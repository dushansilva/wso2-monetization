/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.wso2;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.wso2.apim.monetization.impl.StripeMonetizationConstants;
import org.wso2.apim.monetization.impl.StripeMonetizationDAO;
import org.wso2.apim.monetization.impl.StripeMonetizationException;
import org.wso2.apim.monetization.impl.model.MonetizationPlatformCustomer;
import org.wso2.apim.monetization.impl.model.MonetizationSharedCustomer;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.model.*;
import org.wso2.carbon.apimgt.api.model.policy.SubscriptionPolicy;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.SubscriptionWorkflowDTO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.HttpWorkflowResponse;
import org.wso2.carbon.apimgt.impl.workflow.GeneralWorkflowResponse;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowConstants;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.impl.APIManagerFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Workflow executor for stripe based subscription create action
 */
public class ExtendedStripeSubscriptionCreationWorkflowExecutor extends WorkflowExecutor {

    private static final Log log = LogFactory.getLog(ExtendedStripeSubscriptionCreationWorkflowExecutor.class);
    StripeMonetizationDAO stripeMonetizationDAO = StripeMonetizationDAO.getInstance();

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_SUBSCRIPTION_CREATION;
    }

    @Override
    public List<WorkflowDTO> getWorkflowDetails(String workflowStatus) throws WorkflowException {
        return null;
    }

    /**
     * This method executes subscription creation workflow and return workflow response back to the caller
     *
     * @param workflowDTO The WorkflowDTO which contains workflow contextual information related to the workflow
     * @return workflow response back to the caller
     * @throws WorkflowException Thrown when the workflow execution was not fully performed
     */
    @Override
    public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {

        super.execute(workflowDTO);
        SubscriptionWorkflowDTO subWorkFlowDTO = (SubscriptionWorkflowDTO) workflowDTO;
        Map<String, String> attributes = subWorkFlowDTO.getAttributes();

        if (Boolean.parseBoolean(attributes.get("cardDetailsAvailable"))) {
            WorkflowResponse workflowResponse = complete(workflowDTO);
            return workflowResponse;
        }

        String name = subWorkFlowDTO.getTierName();
        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
        try {
            SubscriptionPolicy policy = apiMgtDAO.getSubscriptionPolicy(name, workflowDTO.getTenantId());
            if (policy.getBillingPlan().equalsIgnoreCase(APIConstants.COMMERCIAL_TIER_PLAN)) {
                HttpWorkflowResponse httpWorkflowResponse = new HttpWorkflowResponse();
                httpWorkflowResponse.setAdditionalParameters("sessionId",
                        workflowDTO.getAttributes().get("sessionId"));
                httpWorkflowResponse.setAdditionalParameters("stripePublishableKey",
                        workflowDTO.getAttributes().get("stripePublishableKey"));
                super.publishEvents(workflowDTO);
                return httpWorkflowResponse;
            } else {
                WorkflowResponse workflowResponse = complete(workflowDTO);
                return workflowResponse;
            }

        } catch (APIManagementException e) {
            String errorMessage = "Error when getting subscription policy";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        }
    }

    /**
     * This method executes monetization related functions in the subscription creation workflow
     *
     * @param workflowDTO The WorkflowDTO which contains workflow contextual information related to the workflow
     * @param api         API
     * @return workflow response back to the caller
     * @throws WorkflowException Thrown when the workflow execution was not fully performed
     */
    @Override
    public WorkflowResponse monetizeSubscription(WorkflowDTO workflowDTO, API api) throws WorkflowException {
        SubscriptionWorkflowDTO subWorkFlowDTO = (SubscriptionWorkflowDTO) workflowDTO;
        Stripe.apiKey = getPlatformAccountKey(subWorkFlowDTO.getTenantId(),
                StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> attributes = new HashMap<>();
        ArrayList<String> paymentMethodTypes = new ArrayList<>();
        String devportalUrl = null;
        ApiTypeWrapper apiTypeWrapper = new ApiTypeWrapper(api);
        try {
            //Check if card details/subscriber details are already available and create subscription if they
            // are available.
            if (handleCardDetailsAvailable(subWorkFlowDTO, apiTypeWrapper)) {
                attributes.put("cardDetailsAvailable", "true");
                workflowDTO.setAttributes(attributes);
                return execute(workflowDTO);
            }

            String apiUUID = api.getUUID();
            devportalUrl = APIUtil.getStoreUrl();
            paymentMethodTypes.add("card");
            params.put("payment_method_types", paymentMethodTypes);
            params.put("mode", "setup");
            params.put("success_url", devportalUrl + "/apis/" + apiUUID +
                    "/checkout-success?sessionId={CHECKOUT_SESSION_ID}&workflowId=" +
                    workflowDTO.getExternalWorkflowReference());
            params.put("cancel_url", devportalUrl + "/apis/" + apiUUID + "/checkout-failed");
            String platformPublishableKey = getPlatformAccountKey(subWorkFlowDTO.getTenantId(),
                    "StripePlatformAccountPublishableKey");
            Session session = Session.create(params);

            attributes.put("sessionId", session.getId());
            attributes.put("stripePublishableKey", platformPublishableKey);
            workflowDTO.setAttributes(attributes);
            return execute(workflowDTO);
        } catch (StripeException e) {
            String errorMessage = "Error when creating checkout session for " + subWorkFlowDTO.getSubscriber();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        } catch (APIManagementException e) {
            String errorMessage = "Error when getting devportal url";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        }
    }

    @Override
    public WorkflowResponse monetizeSubscription(WorkflowDTO workflowDTO, APIProduct apiProduct)
            throws WorkflowException {
        SubscriptionWorkflowDTO subWorkFlowDTO = (SubscriptionWorkflowDTO) workflowDTO;
        Stripe.apiKey = getPlatformAccountKey(subWorkFlowDTO.getTenantId(),
                StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, String> attributes = new HashMap<>();
        ArrayList<String> paymentMethodTypes = new ArrayList<>();
        String devportalUrl = null;
        ApiTypeWrapper apiTypeWrapper = new ApiTypeWrapper(apiProduct);
        try {
            //Check if card details/subscriber details are already available and create subscription if they
            // are available.
            if (handleCardDetailsAvailable(subWorkFlowDTO, apiTypeWrapper)) {
                attributes.put("cardDetailsAvailable", "true");
                workflowDTO.setAttributes(attributes);
                return execute(workflowDTO);
            }
            String apiUUID = apiProduct.getUuid();
            devportalUrl = APIUtil.getStoreUrl();
            paymentMethodTypes.add("card");
            params.put("payment_method_types", paymentMethodTypes);
            params.put("mode", "setup");
            params.put("success_url", devportalUrl + "/apis/" + apiUUID +
                    "/checkout-success?sessionId={CHECKOUT_SESSION_ID}&workflowId=" +
                    workflowDTO.getExternalWorkflowReference());
            params.put("cancel_url", devportalUrl + "/apis/" + apiUUID + "/checkout-failed");
            String platformPublishableKey = getPlatformAccountKey(subWorkFlowDTO.getTenantId(),
                    "StripePlatformAccountPublishableKey");

            Session session = Session.create(params);
            workflowDTO.setAttributes(attributes);
            attributes.put("sessionId", session.getId());
            attributes.put("stripePublishableKey", platformPublishableKey);

        } catch (StripeException e) {
            String errorMessage = "Error when creating checkout session for " + subWorkFlowDTO.getSubscriber();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        } catch (APIManagementException e) {
            String errorMessage = "Error when getting devportal url";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        }

        return execute(workflowDTO);
    }

    /**
     * Returns the stripe key of the platform/tenant
     *
     * @param tenantId id of the tenant
     * @return the stripe key of the platform/tenant
     * @throws WorkflowException
     */
    private String getPlatformAccountKey(int tenantId, String keyType) throws WorkflowException {

        Object stripePlatformAccountKey = null;
        try {
            Registry configRegistry = ServiceReferenceHolder.getInstance().getRegistryService().getConfigSystemRegistry(
                    tenantId);
            if (configRegistry.resourceExists(APIConstants.API_TENANT_CONF_LOCATION)) {
                Resource resource = configRegistry.get(APIConstants.API_TENANT_CONF_LOCATION);
                String content = new String((byte[]) resource.getContent(), Charset.defaultCharset());

                if (StringUtils.isBlank(content)) {
                    String errorMessage = "Tenant configuration cannot be empty when configuring monetization.";
                    throw new WorkflowException(errorMessage);
                }
                //get the stripe key of patform account from tenant conf file
                JSONObject tenantConfig = (JSONObject) new JSONParser().parse(content);
                JSONObject monetizationInfo = (JSONObject) tenantConfig.get(
                        StripeMonetizationConstants.MONETIZATION_INFO);
                stripePlatformAccountKey = monetizationInfo.get(keyType);

                if (stripePlatformAccountKey != null && StringUtils.isBlank(stripePlatformAccountKey.toString())) {
                    throw new WorkflowException(keyType + " is empty!!!");
                }
            }
        } catch (RegistryException ex) {
            throw new WorkflowException("Could not get all registry objects : ", ex);
        } catch (org.json.simple.parser.ParseException ex) {
            throw new WorkflowException("Could not get Stripe Platform key : ", ex);
        }
        return stripePlatformAccountKey.toString();
    }

    /**
     * The method creates a Shared Customer in billing engine
     *
     * @param email            Email of the subscriber
     * @param platformCustomer Monetization customer details created under platform account
     * @param requestOptions   contains credentials to make api requests on behalf of the connected account
     * @param subWorkFlowDTO   The WorkflowDTO which contains workflow contextual information related to the workflow
     * @return MonetizationSharedCustomer Object with the details of the created shared customer
     * @throws WorkflowException
     */
    public MonetizationSharedCustomer createSharedCustomer(String email, MonetizationPlatformCustomer platformCustomer,
                                                           RequestOptions requestOptions,
                                                           SubscriptionWorkflowDTO subWorkFlowDTO, String paymentMethod)
            throws WorkflowException {

        Customer stripeCustomer;
        MonetizationSharedCustomer monetizationSharedCustomer = new MonetizationSharedCustomer();
        Token token;

        Map<String, Object> sharedCustomerParams = new HashMap<>();
        //if the email id of subscriber is empty, a customer object in billing engine will be created without email id
        if (!StringUtils.isEmpty(email)) {
            sharedCustomerParams.put(StripeMonetizationConstants.CUSTOMER_EMAIL, email);
        }
        try {
            sharedCustomerParams.put(StripeMonetizationConstants.CUSTOMER_DESCRIPTION, "Shared Customer for "
                    + subWorkFlowDTO.getApplicationName() + StripeMonetizationConstants.FILE_SEPERATOR
                    + subWorkFlowDTO.getSubscriber());
            stripeCustomer = Customer.create(sharedCustomerParams, requestOptions);

            //Copy payment method from platform customer and create it in connected account
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customer", platformCustomer.getCustomerId());
            params.put("payment_method", paymentMethod);
            PaymentMethod sharedPaymentMethod = PaymentMethod.create(params, requestOptions);

            //Attach the created payment method to the shared customer
            Map<String, Object> invoiceSettings = new HashMap<>();
            PaymentMethod method = PaymentMethod.retrieve(sharedPaymentMethod.getId(), requestOptions);
            Map<String, Object> payParams = new HashMap<String, Object>();
            payParams.put("customer", stripeCustomer.getId());
            method.attach(payParams, requestOptions);

            //Attaching the default payment method for the subscription
            Map<String, Object> customerUpdateParams = new HashMap<String, Object>();
            invoiceSettings.put("default_payment_method", sharedPaymentMethod.getId());
            customerUpdateParams.put("invoice_settings", invoiceSettings);
            stripeCustomer.update(customerUpdateParams, requestOptions);

            try {
                monetizationSharedCustomer.setApplicationId(subWorkFlowDTO.getApplicationId());
                monetizationSharedCustomer.setApiProvider(subWorkFlowDTO.getApiProvider());
                monetizationSharedCustomer.setTenantId(subWorkFlowDTO.getTenantId());
                monetizationSharedCustomer.setSharedCustomerId(stripeCustomer.getId());
                monetizationSharedCustomer.setParentCustomerId(platformCustomer.getId());
                //returns the ID of the inserted record
                int id = stripeMonetizationDAO.addBESharedCustomer(monetizationSharedCustomer);
                monetizationSharedCustomer.setId(id);

            } catch (StripeMonetizationException ex) {
                //deleting the created customer in stripe if it fails to create the DB record
                stripeCustomer.delete(requestOptions);
                String errorMsg = "Error when inserting Stripe shared customer details of Application : "
                        + subWorkFlowDTO.getApplicationName() + "to database";
                log.error(errorMsg, ex);
                throw new WorkflowException(errorMsg, ex);
            }
            if (log.isDebugEnabled()) {
                String msg = "A customer for Application " + subWorkFlowDTO.getApplicationName()
                        + " is created under the " + subWorkFlowDTO.getApiProvider()
                        + "'s connected account in Stripe";
                log.debug(msg);
            }
        } catch (StripeException ex) {
            String errorMsg = "Error while creating a shared customer in Stripe for Application : "
                    + subWorkFlowDTO.getApplicationName();
            log.error(errorMsg);
            throw new WorkflowException(errorMsg, ex);
        }
        return monetizationSharedCustomer;
    }

    /**
     * The method creates a subscription in Billing Engine
     *
     * @param planId         plan Id of the Stripe monetization plan
     * @param sharedCustomer contains info about the customer created in the provider account of Stripe
     * @param requestOptions contains connected account credential needed for Stripe transactions
     * @param subWorkFlowDTO The WorkflowDTO which contains workflow contextual information related to the workflow
     * @throws WorkflowException
     */
    public void createMonetizedSubscriptions(String planId, MonetizationSharedCustomer sharedCustomer,
                                             RequestOptions requestOptions, SubscriptionWorkflowDTO subWorkFlowDTO)
            throws WorkflowException {

        StripeMonetizationDAO stripeMonetizationDAO = StripeMonetizationDAO.getInstance();
        APIIdentifier identifier = new APIIdentifier(subWorkFlowDTO.getApiProvider(), subWorkFlowDTO.getApiName(),
                subWorkFlowDTO.getApiVersion());
        Subscription subscription = null;
        try {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put(StripeMonetizationConstants.PLAN, planId);
            Map<String, Object> items = new HashMap<String, Object>();
            //adding a subscription item, with an attached plan.
            items.put("0", item);
            Map<String, Object> subParams = new HashMap<String, Object>();
            subParams.put(StripeMonetizationConstants.CUSTOMER, sharedCustomer.getSharedCustomerId());
            subParams.put(StripeMonetizationConstants.ITEMS, items);
            try {
                //create a subscritpion in stripe under the API Providers Connected Account
                subscription = Subscription.create(subParams, requestOptions);
            } catch (StripeException ex) {
                String errorMsg = "Error when adding a subscription in Stripe for Application : " +
                        subWorkFlowDTO.getApplicationName();
                log.error(errorMsg);
                throw new WorkflowException(errorMsg, ex);
            }
            try {
                stripeMonetizationDAO.addBESubscription(identifier, subWorkFlowDTO.getApplicationId(),
                        subWorkFlowDTO.getTenantId(), sharedCustomer.getId(), subscription.getId());
            } catch (StripeMonetizationException e) {
                //delete the subscription in Stripe, if the entry to database fails in API Manager
                subscription.cancel((Map<String, Object>) null, requestOptions);
                String errorMsg = "Error when adding stripe subscription details of Application "
                        + subWorkFlowDTO.getApplicationName() + " to Database";
                log.error(errorMsg);
                throw new WorkflowException(errorMsg, e);
            }
            if (log.isDebugEnabled()) {
                String msg = "Stripe subscription for " + subWorkFlowDTO.getApplicationName() + " is created for"
                        + subWorkFlowDTO.getApiName() + " API";
                log.debug(msg);
            }
        } catch (StripeException ex) {
            String errorMessage = "Failed to create subscription in Stripe for API : " + subWorkFlowDTO.getApiName()
                    + "by Application : " + subWorkFlowDTO.getApplicationName();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage, ex);
        }
    }

    /**
     * The method creates a Platform Customer in Billing Engine
     *
     * @param subscriber object which contains info about the subscriber
     * @return StripeCustomer object which contains info about the customer created in platform account of stripe
     * @throws WorkflowException
     */
    public MonetizationPlatformCustomer createMonetizationPlatformCustomer(Subscriber subscriber, String paymentMethod)
            throws WorkflowException {

        MonetizationPlatformCustomer monetizationPlatformCustomer = new MonetizationPlatformCustomer();
        Customer customer = null;
        try {
            Map<String, Object> customerParams = new HashMap<String, Object>();
            //Customer object in billing engine will be created without the email id
            if (!StringUtils.isEmpty(subscriber.getEmail())) {
                customerParams.put(StripeMonetizationConstants.CUSTOMER_EMAIL, subscriber.getEmail());
            }
            customerParams.put(StripeMonetizationConstants.CUSTOMER_DESCRIPTION, "Customer for "
                    + subscriber.getName());

            //create a customer for subscriber in the platform account
            customer = Customer.create(customerParams);

            monetizationPlatformCustomer.setCustomerId(customer.getId());

            Map<String, Object> invoiceSettings = new HashMap<>();
            PaymentMethod method = PaymentMethod.retrieve(paymentMethod);
            Map<String, Object> payParams = new HashMap<String, Object>();
            payParams.put("customer", customer.getId());
            method.attach(payParams);

            Map<String, Object> customerUpdateParams = new HashMap<String, Object>();
            invoiceSettings.put("default_payment_method", paymentMethod);

            //Attaching the default payment method for the subscription
            customerUpdateParams.put("invoice_settings", invoiceSettings);
            customer.update(customerUpdateParams);

            try {
                //returns the id of the inserted record
                int id = stripeMonetizationDAO.addBEPlatformCustomer(subscriber.getId(), subscriber.getTenantId(),
                        customer.getId());
                monetizationPlatformCustomer.setId(id);
            } catch (StripeMonetizationException e) {
                if (customer != null) {
                    // deletes the customer if the customer is created in Stripe and failed to update in DB
                    customer.delete();
                }
                String errorMsg = "Error when inserting stripe customer details of " + subscriber.getName()
                        + " to Database";
                log.error(errorMsg);
                throw new WorkflowException(errorMsg, e);
            }
        } catch (StripeException ex) {
            String errorMsg = "Error while creating a customer in Stripe for " + subscriber.getName();
            log.error(errorMsg);
            throw new WorkflowException(errorMsg, ex);
        }
        return monetizationPlatformCustomer;
    }

    /**
     * This method completes subscription creation workflow and return workflow response back to the caller
     *
     * @param workflowDTO The WorkflowDTO which contains workflow contextual information related to the workflow
     * @return workflow response back to the caller
     * @throws WorkflowException
     */
    @Override
    public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {

        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
        Map<String, String> attributes = workflowDTO.getAttributes();

        try {
            if (Boolean.parseBoolean(attributes.get("cardDetailsAvailable"))) {
                apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                        APIConstants.SubscriptionStatus.UNBLOCKED);
                return new GeneralWorkflowResponse();
            }

            SubscribedAPI subscribedAPI = apiMgtDAO.getSubscriptionById(Integer.parseInt(workflowDTO.getWorkflowReference()));
            SubscriptionPolicy policy = apiMgtDAO.getSubscriptionPolicy(subscribedAPI.getTier().getName(), workflowDTO.getTenantId());

            if (APIConstants.COMMERCIAL_TIER_PLAN.equalsIgnoreCase(policy.getBillingPlan())) {
                SubscriptionWorkflowDTO subWorkFlowDTO = (SubscriptionWorkflowDTO) workflowDTO;
                handleMonatizedWorkflow(apiMgtDAO, subscribedAPI, subWorkFlowDTO);
            }

            apiMgtDAO.updateSubscriptionStatus(Integer.parseInt(workflowDTO.getWorkflowReference()),
                    APIConstants.SubscriptionStatus.UNBLOCKED);
            return new GeneralWorkflowResponse();
        } catch (APIManagementException e) {
            log.error("Could not complete subscription creation workflow", e);
            throw new WorkflowException("Could not complete subscription creation workflow", e);
        }
    }

    /**
     * Implement of the process of creating customers and subscriptions in stripe
     * and saving the details in databases
     *
     * @param apiMgtDAO
     * @param subscribedAPI
     * @param subWorkFlowDTO
     * @throws WorkflowException
     */
    private void handleMonatizedWorkflow(ApiMgtDAO apiMgtDAO, SubscribedAPI subscribedAPI,
                                         SubscriptionWorkflowDTO subWorkFlowDTO) throws WorkflowException {
        try {

            APIIdentifier apiIdentifier = subscribedAPI.getApiId();
            APIProductIdentifier apiProductIdentifier = subscribedAPI.getProductId();
            APIConsumer apiConsumer = getLoggedInUserConsumer();
            Map<String, String> attributes = subWorkFlowDTO.getAttributes();
            Map<String, String> monetizationProperties = new HashMap<>();
            Gson gson = new Gson();
            int apiId = 0;
            String apiName = StringUtils.EMPTY;
            String apiVersion = StringUtils.EMPTY;
            String apiProvider = StringUtils.EMPTY;

            //Handle api product and api cases
            if (apiProductIdentifier != null) {
                APIProduct apiProduct = apiConsumer.getAPIProduct(apiProductIdentifier);
                apiName = apiProductIdentifier.getName();
                apiVersion = apiProductIdentifier.getVersion();
                apiProvider = apiProductIdentifier.getProviderName();
                monetizationProperties.putAll(gson.fromJson(apiProduct.getMonetizationProperties().toString(),
                        HashMap.class));
                apiId = apiMgtDAO.getAPIID(apiProduct.getId(), null);
            }
            if (apiIdentifier != null) {
                API api = apiConsumer.getAPI(apiIdentifier);
                apiName = apiIdentifier.getApiName();
                apiVersion = apiIdentifier.getVersion();
                apiProvider = apiIdentifier.getProviderName();
                monetizationProperties.putAll(gson.fromJson(api.getMonetizationProperties().toString(),
                        HashMap.class));
                apiId = apiMgtDAO.getAPIID(api.getId(), null);
            }

            subWorkFlowDTO.setApiName(apiName);
            subWorkFlowDTO.setApiProvider(apiProvider);
            subWorkFlowDTO.setApiVersion(apiVersion);
            subWorkFlowDTO.setSubscriber(subscribedAPI.getSubscriber().getName());
            subWorkFlowDTO.setApplicationId(subscribedAPI.getApplication().getId());
            subWorkFlowDTO.setTierName(subscribedAPI.getTier().getName());
            subWorkFlowDTO.setApplicationName(subscribedAPI.getApplication().getName());
            subWorkFlowDTO.setTenantId(APIUtil.getTenantId(subWorkFlowDTO.getSubscriber()));

            MonetizationPlatformCustomer monetizationPlatformCustomer;
            MonetizationSharedCustomer monetizationSharedCustomer;
            Subscriber subscriber = apiMgtDAO.getSubscriber(subWorkFlowDTO.getSubscriber());

            //read the platform account key of Stripe
            Stripe.apiKey = getPlatformAccountKey(subWorkFlowDTO.getTenantId(),
                    StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);
            String sessionId = attributes.get("sessionId");
            if (StringUtils.isBlank(sessionId)) {
                String errorMessage = "Error session id not in the request";
                log.error(errorMessage);
                throw new WorkflowException(errorMessage);
            }

            String connectedAccountKey = getConnectedAccountKey(monetizationProperties);

            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(connectedAccountKey).build();

            Session session = Session.retrieve(sessionId);
            SetupIntent setupIntent = SetupIntent.retrieve(session.getSetupIntent());
            String paymentMethod = setupIntent.getPaymentMethod();

            if (StringUtils.isBlank(paymentMethod)) {
                String errorMessage = "Payment details does not exists";
                log.error(errorMessage);
                throw new WorkflowException(errorMessage);
            }

            // check whether the application is already registered as a customer under the particular
            // APIprovider/Connected Account in Stripe
            monetizationSharedCustomer = stripeMonetizationDAO.getSharedCustomer(subWorkFlowDTO.getApplicationId(),
                    subWorkFlowDTO.getApiProvider(), subWorkFlowDTO.getTenantId());
            if (monetizationSharedCustomer.getSharedCustomerId() == null) {
                // checks whether the subscriber is already registered as a customer Under the
                // tenant/Platform account in Stripe
                monetizationPlatformCustomer = stripeMonetizationDAO.getPlatformCustomer(subscriber.getId(),
                        subscriber.getTenantId());
                if (monetizationPlatformCustomer.getCustomerId() == null) {
                    monetizationPlatformCustomer = createMonetizationPlatformCustomer(subscriber, paymentMethod);
                }
                monetizationSharedCustomer = createSharedCustomer(subscriber.getEmail(), monetizationPlatformCustomer,
                        requestOptions, subWorkFlowDTO, paymentMethod);
            }
            //creating Subscriptions
            String planId = stripeMonetizationDAO.getBillingEnginePlanIdForTier(apiId, subWorkFlowDTO.getTierName());
            createMonetizedSubscriptions(planId, monetizationSharedCustomer, requestOptions, subWorkFlowDTO);
        } catch (APIManagementException e) {
            String errorMessage = "Could not monetize subscription for API : " + subWorkFlowDTO.getApiName()
                    + " by Application : " + subWorkFlowDTO.getApplicationName();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage, e);
        } catch (StripeMonetizationException e) {
            String errorMessage = "Could not monetize subscription for API : " + subWorkFlowDTO.getApiName()
                    + " by Application " + subWorkFlowDTO.getApplicationName();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage, e);
        } catch (StripeException e) {
            String errorMessage = "Error when retrieving stripe session: " + subWorkFlowDTO.getApiName()
                    + " by Application " + subWorkFlowDTO.getApplicationName();
            log.error(errorMessage);
            throw new WorkflowException(errorMessage, e);
        }
    }

    /**
     * Get connected account key
     *
     * @param monetizationProperties
     * @return
     * @throws WorkflowException
     */
    private String getConnectedAccountKey(Map<String, String> monetizationProperties) throws WorkflowException {

        if (MapUtils.isNotEmpty(monetizationProperties) && monetizationProperties
                .containsKey(StripeMonetizationConstants.BILLING_ENGINE_CONNECTED_ACCOUNT_KEY)) {

            String connectedAccountKey = monetizationProperties
                    .get(StripeMonetizationConstants.BILLING_ENGINE_CONNECTED_ACCOUNT_KEY);

            if (StringUtils.isBlank(connectedAccountKey)) {
                String errorMessage = "Connected account stripe key was not found ";
                log.error(errorMessage);
                throw new WorkflowException(errorMessage);
            }
            return connectedAccountKey;
        } else {
            String errorMessage = "Stripe key of the connected account is empty.";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        }
    }

    /**
     * Check if customer details exists already if it does not return false. If platform customer details exists
     * use that to create the shared customer and the subscription
     *
     * @param subWorkFlowDTO
     * @param model
     * @return
     * @throws WorkflowException
     */
    private boolean handleCardDetailsAvailable(SubscriptionWorkflowDTO subWorkFlowDTO, ApiTypeWrapper model) throws WorkflowException {

        Map<String, String> monetizationProperties = new HashMap<>();
        Identifier apiIdentifer = null;
        Gson gson = new Gson();

        if (model.isAPIProduct()) {
            APIProduct apiProduct = model.getApiProduct();
            apiIdentifer = apiProduct.getId();
            monetizationProperties.putAll(gson.fromJson(apiProduct.getMonetizationProperties().toString(), HashMap.class));
        } else {
            API api = model.getApi();
            apiIdentifer = api.getId();
            monetizationProperties.putAll(gson.fromJson(api.getMonetizationProperties().toString(), HashMap.class));
        }

        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
        try {
            Subscriber subscriber = apiMgtDAO.getSubscriber(subWorkFlowDTO.getSubscriber());
            //Check if platform customer details exists
            MonetizationPlatformCustomer monetizationPlatformCustomer =
                    stripeMonetizationDAO.getPlatformCustomer(subscriber.getId(), subscriber.getTenantId());
            if (monetizationPlatformCustomer.getCustomerId() == null) {
                return false;
            }

            //check if shared customer payment details available. If not create shared customer
            MonetizationSharedCustomer monetizationSharedCustomer =
                    stripeMonetizationDAO.getSharedCustomer(subWorkFlowDTO.getApplicationId(),
                            subWorkFlowDTO.getApiProvider(), subWorkFlowDTO.getTenantId());
            String connectedAccountKey = getConnectedAccountKey(monetizationProperties);
            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(connectedAccountKey).build();

            if (monetizationSharedCustomer.getSharedCustomerId() == null) {
                //Retrieve platform customer to get payment method
                Customer platformCustomer = Customer.retrieve(monetizationPlatformCustomer.getCustomerId());
                String platformPaymentMethod = platformCustomer.getInvoiceSettings().getDefaultPaymentMethod();
                monetizationSharedCustomer = createSharedCustomer(subscriber.getEmail(), monetizationPlatformCustomer,
                        requestOptions, subWorkFlowDTO, platformPaymentMethod);
            }

            int apiId = apiMgtDAO.getAPIID(apiIdentifer, null);
            String planId = stripeMonetizationDAO.getBillingEnginePlanIdForTier(apiId, subWorkFlowDTO.getTierName());
            createMonetizedSubscriptions(planId, monetizationSharedCustomer, requestOptions, subWorkFlowDTO);
            return true;
        } catch (StripeMonetizationException | APIManagementException e) {
            String errorMessage = "Error when accessing subscriber details from db";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        } catch (StripeException e) {
            String errorMessage = "Error when accessing subscriber details in stripe";
            log.error(errorMessage);
            throw new WorkflowException(errorMessage);
        }
    }

    private APIConsumer getLoggedInUserConsumer() throws APIManagementException {
        return APIManagerFactory.getInstance().getAPIConsumer(getLoggedInUsername());
    }

    private String getLoggedInUsername() {
        return CarbonContext.getThreadLocalCarbonContext().getUsername();
    }
}
