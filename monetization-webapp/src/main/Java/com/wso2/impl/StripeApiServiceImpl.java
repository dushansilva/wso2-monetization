/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wso2.*;
import com.wso2.dao.StripeDao;
import com.wso2.dto.*;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.MessageContext;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.wso2.apim.monetization.impl.StripeMonetizationConstants;
import org.wso2.apim.monetization.impl.StripeMonetizationDAO;
import org.wso2.apim.monetization.impl.StripeMonetizationException;
import org.wso2.apim.monetization.impl.model.MonetizationPlatformCustomer;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.API;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.APIInfoDTO;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;


import javax.ws.rs.core.Response;


public class StripeApiServiceImpl implements StripeApiService {
    private static final Log log = LogFactory.getLog(StripeApiServiceImpl.class);
    StripeMonetizationDAO stripeMonetizationDAO = StripeMonetizationDAO.getInstance();
    StripeDao stripeDao = StripeDao.getInstance();

    @Override
    public Response stripeBillingDetailsGet(MessageContext messageContext) {
        String username = RestApiUtil.getLoggedInUsername();
        int tenantId = APIUtil.getTenantId(username);
        try {
            APIConsumer apiConsumer = RestApiUtil.getLoggedInUserConsumer();
            //read the platform account key of Stripe
            Stripe.apiKey = getPlatformAccountKey(tenantId,
                    StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);

            Subscriber subscriber = apiConsumer.getSubscriber(username);

            MonetizationPlatformCustomer platformCustomer =
                    stripeMonetizationDAO.getPlatformCustomer(subscriber.getId(), tenantId);
            if (StringUtils.isBlank(platformCustomer.getCustomerId())) {
                RestApiUtil.handleResourceNotFoundError("No card details found ", log);
            }
            Customer customer = Customer.retrieve(platformCustomer.getCustomerId());
            String customerPaymentMethod = customer.getInvoiceSettings().getDefaultPaymentMethod();
            if (!StringUtils.isBlank(customerPaymentMethod)) {
                PaymentMethod paymentMethod = PaymentMethod.
                        retrieve(customerPaymentMethod);
                PaymentMethod.Card card = paymentMethod.getCard();
                StripeCardDetailsResponseDTO cardDetailsResponseDTO = new StripeCardDetailsResponseDTO();
                cardDetailsResponseDTO.setBrand(card.getBrand());
                cardDetailsResponseDTO.expMonth(BigDecimal.valueOf(card.getExpMonth()));
                cardDetailsResponseDTO.expYear(BigDecimal.valueOf(card.getExpYear()));
                cardDetailsResponseDTO.setLast4(card.getLast4());
                return Response.ok().entity(cardDetailsResponseDTO).build();
            } else {
                RestApiUtil.handleResourceNotFoundError("No card details found ", log);
            }

        } catch (StripeMonetizationException e) {
            RestApiUtil.handleInternalServerError("Error while getting platform customer from database ", e, log);
        } catch (StripeException e) {
            RestApiUtil.handleInternalServerError("Error while getting platform customer from stripe ", e, log);
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while getting getting billing details ", e, log);
        }
        return null;
    }

    @Override
    public Response stripeSetupCardSessionApiIdPost(String apiId, MessageContext messageContext) {
        String username = RestApiUtil.getLoggedInUsername();
        int tenantId = APIUtil.getTenantId(username);
        Map<String, Object> params = new HashMap<String, Object>();
        ArrayList<String> paymentMethodTypes = new ArrayList<>();

        try {
            String devportalUrl = APIUtil.getStoreUrl();
            Stripe.apiKey = getPlatformAccountKey(tenantId, StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);

            //add required details to create session
            paymentMethodTypes.add("card");
            params.put("payment_method_types", paymentMethodTypes);
            params.put("mode", "setup");
            params.put("client_reference_id", apiId);
            params.put("success_url", devportalUrl + "/apis/" + apiId +
                    "/payment-details?sessionId={CHECKOUT_SESSION_ID}");
            params.put("cancel_url", devportalUrl + "/apis/" + apiId + "/checkout-failed");
            String platformPublishableKey = getPlatformAccountKey(tenantId,
                    "StripePlatformAccountPublishableKey");

            Session session = Session.create(params);
            SetupCardSessionResponseDTO sessionResponseDTO = new SetupCardSessionResponseDTO();
            sessionResponseDTO.setSessionId(session.getId());
            sessionResponseDTO.setStripePublishableKey(platformPublishableKey);
            return Response.ok().entity(sessionResponseDTO).build();
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while getting platform account keys ", e, log);
        } catch (StripeException e) {
            RestApiUtil.handleInternalServerError("Error while Creating stripe session ", e, log);
        }
        return null;
    }

    @Override
    public Response stripeUpdateStripeCardPost(UpdateStripeCardRequestObjectDTO body, MessageContext messageContext) {
        if (body == null) {
            RestApiUtil.handleBadRequest("Request payload is missing", log);
        }

        String sessionId = body.getSessionId();
        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
        String username = RestApiUtil.getLoggedInUsername();
        int tenantId = APIUtil.getTenantId(username);

        if (StringUtils.isBlank(sessionId)) {
            String errorMessage = "Error session id not in the request";
            log.error(errorMessage);
            RestApiUtil.handleInternalServerError(errorMessage, log);
        }
        try {
            APIConsumer apiConsumer = RestApiUtil.getLoggedInUserConsumer();
            //read the platform account key of Stripe
            Stripe.apiKey = getPlatformAccountKey(tenantId,
                    StripeMonetizationConstants.BILLING_ENGINE_PLATFORM_ACCOUNT_KEY);

            Session session = Session.retrieve(sessionId);
            SetupIntent setupIntent = SetupIntent.retrieve(session.getSetupIntent());
            String paymentMethod = setupIntent.getPaymentMethod();
            APIInfoDTO[] subscribedAPIsOfUser = apiMgtDAO.getSubscribedAPIsOfUser(username);
            Map<Integer, String> apiIdToConnectedAccountKeys = new HashMap<>();
            //Get all connected account keys to be updated
            for (APIInfoDTO apiInfo : subscribedAPIsOfUser) {
                APIIdentifier apiIdentifier = new APIIdentifier(apiInfo.getAPIIdentifier());
                API api = apiConsumer.getAPI(apiIdentifier);
                int apiId = apiMgtDAO.getAPIID(apiIdentifier, null);
                Map<String, String> monetizationProperties = new Gson().fromJson(api.getMonetizationProperties().toString(),
                        HashMap.class);
                String connectedAccountKey = getConnectedAccountKey(monetizationProperties);
                apiIdToConnectedAccountKeys.put(apiId, connectedAccountKey);
            }
            //Update platform account card details
            Subscriber subscriber = apiConsumer.getSubscriber(username);
            MonetizationPlatformCustomer platformCustomer =
                    stripeMonetizationDAO.getPlatformCustomer(subscriber.getId(), tenantId);
            updatePlatformCustomerCard(platformCustomer.getCustomerId(), paymentMethod);
            // Update Shared customer card details. All shared customer objects created relating to this subscriber id
            // will be updated
            List<CardUpdateDetailsDTO> cardUpdateRequiredDetails = stripeDao.getCardUpdateRequiredDetails(subscriber.getId());
            for (CardUpdateDetailsDTO detail : cardUpdateRequiredDetails) {
                String sharedCustomerId = detail.getSharedCustomerId();
                String platformCustomerId = detail.getParentCustomerId();
                String connectedAccountKey = apiIdToConnectedAccountKeys.get(detail.getSubscribedApiId());
                RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(connectedAccountKey).build();
                updateSharedCustomerCard(sharedCustomerId, platformCustomerId, requestOptions, paymentMethod);
            }
            Response.ok().build();
        } catch (StripeException e) {
            RestApiUtil.handleInternalServerError("Error while retrieving stripe session ", e, log);
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while retrieving connected account key ", e, log);
        } catch (StripeMonetizationException e) {
            RestApiUtil.handleInternalServerError("Error while updating card details", e, log);
        }
        return null;
    }

    @Override
    public Response stripeWebhookWebhookKeyPost(String webhookKey, String body, MessageContext messageContext) {
        Event event = null;
        String signature = messageContext.getHttpHeaders().getHeaderString("Stripe-Signature");
        if (StringUtils.isBlank(signature)) {
            log.error("Web hook signature verification failed, stripe signature header is missing");
            RestApiUtil.handleAuthorizationFailure("Not permitted to access this resource ", "", log);
        }

        try {
            if (StringUtils.isBlank(webhookKey)) {
                log.error("Web hook signature verification failed, stripe web hook key is missing in path");
                RestApiUtil.handleAuthorizationFailure("Not permitted to access this resource ", "", log);
            }

            String stripeWebhookKey = getPlatformAccountKey(APIUtil.getSuperTenantId(), webhookKey);
            event = Webhook.constructEvent(
                    body, signature, stripeWebhookKey
            );
        } catch (JsonSyntaxException | SignatureVerificationException | APIManagementException e) {
            log.error("Web hook signature verification failed", e);
            RestApiUtil.handleAuthorizationFailure("Not permitted to access this resource ", "", log);
        }

        if ("invoice.payment_failed".equalsIgnoreCase(event.getType())) {
            handleInvoicePaymentFailedEvent(event);
        }

        if ("invoice.payment_succeeded".equalsIgnoreCase(event.getType())) {
            handleInvoicePaymentSuccessEvent(event);
        }

        return Response.ok().build();
    }

    private void handleInvoicePaymentSuccessEvent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        Map<String, String> dataMap = new Gson().fromJson(dataObjectDeserializer.getRawJson(), Map.class);
        String stripeSubscriptionId = dataMap.get("subscription");
        if (StringUtils.isBlank(stripeSubscriptionId)) {
            RestApiUtil.handleResourceNotFoundError("No stripe subscription details found ", log);
        }
        try {
            int subscriptionId = stripeDao.getSubscriptionId(stripeSubscriptionId);
            if (subscriptionId == -1) {
                RestApiUtil.handleResourceNotFoundError("No subscription found ", log);
            }

            ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
            SubscribedAPI subscribedAPI = apiMgtDAO.getSubscriptionById(subscriptionId);
            if (APIConstants.SubscriptionStatus.BLOCKED.equalsIgnoreCase(subscribedAPI.getSubStatus())) {
                subscribedAPI.setSubStatus(APIConstants.SubscriptionStatus.UNBLOCKED);
                apiMgtDAO.updateSubscription(subscribedAPI);
                String message = "Subscription with id : " + subscriptionId + " by subscriber " +
                        subscribedAPI.getSubscriber().getName() + " for API " +
                        subscribedAPI.getApiId().getApiName() + " has been unblocked";
                log.info(message);
            }

        } catch (StripeMonetizationException | APIManagementException e) {
            String message = "Error while blocking subscriptions from web hook ";
            RestApiUtil.handleInternalServerError(message, e, log);
        }
    }

    private void handleInvoicePaymentFailedEvent(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        Map<String, String> dataMap = new Gson().fromJson(dataObjectDeserializer.getRawJson(), Map.class);
        String stripeSubscriptionId = dataMap.get("subscription");
        if (StringUtils.isBlank(stripeSubscriptionId)) {
            RestApiUtil.handleResourceNotFoundError("No stripe subscription details found ", log);
        }
        try {
            int subscriptionId = stripeDao.getSubscriptionId(stripeSubscriptionId);
            if (subscriptionId == -1) {
                RestApiUtil.handleResourceNotFoundError("No subscription found ", log);
            }

            ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();
            SubscribedAPI subscribedAPI = apiMgtDAO.getSubscriptionById(subscriptionId);
            subscribedAPI.setSubStatus(APIConstants.SubscriptionStatus.BLOCKED);
            apiMgtDAO.updateSubscription(subscribedAPI);
            String message = "Subscription with id : " + subscriptionId + " by subscriber " +
                    subscribedAPI.getSubscriber().getName() + " for API " +
                    subscribedAPI.getApiId().getApiName() + " blocked due to invoice.payment_failed";
            log.info(message);
        } catch (StripeMonetizationException | APIManagementException e) {
            String message = "Error while blocking subscriptions from web hook ";
            RestApiUtil.handleInternalServerError(message, e, log);
        }
    }

    private void updatePlatformCustomerCard(String platformCustomerId, String paymentMethod) {
        try {
            if (log.isDebugEnabled()) {
                String message = "Parent customer with " + platformCustomerId + " is being updated";
                log.debug(message);
            }
            Customer platformCustomer = Customer.retrieve(platformCustomerId);
            Map<String, Object> invoiceSettings = new HashMap<>();
            PaymentMethod newPaymentMethod = PaymentMethod.retrieve(paymentMethod);

            //Take a reference to the old payment method
            String oldPaymentMethod = platformCustomer.getInvoiceSettings().getDefaultPaymentMethod();
            PaymentMethod oldMethod = PaymentMethod.retrieve(oldPaymentMethod);

            //Attach new payment method to customer
            Map<String, Object> payParams = new HashMap<String, Object>();
            payParams.put("customer", platformCustomer.getId());
            newPaymentMethod.attach(payParams);

            //Setting the new method as the default payment method for the subscription
            Map<String, Object> customerUpdateParams = new HashMap<String, Object>();
            invoiceSettings.put("default_payment_method", newPaymentMethod.getId());
            customerUpdateParams.put("invoice_settings", invoiceSettings);
            platformCustomer.update(customerUpdateParams);

            //remove old payment method
            oldMethod.detach();
        } catch (StripeException e) {
            RestApiUtil.handleInternalServerError("Error while updating platform customer", e, log);
        }
    }

    private void updateSharedCustomerCard(String sharedCustomerId, String platformCustomerId, RequestOptions requestOptions,
                                          String paymentMethod) {
        try {
            if (log.isDebugEnabled()) {
                String message = "shared customer with id " + sharedCustomerId + " belongs to parent " +
                        platformCustomerId + " is being updated";
                log.debug(message);
            }
            Customer sharedCustomer = Customer.retrieve(sharedCustomerId, requestOptions);

            //Take a reference to the old payment method
            String oldPaymentMethod = sharedCustomer.getInvoiceSettings().getDefaultPaymentMethod();
            PaymentMethod oldMethod = PaymentMethod.retrieve(oldPaymentMethod, requestOptions);

            //Copy the new payment method in to the connected account
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customer", platformCustomerId);
            params.put("payment_method", paymentMethod);
            PaymentMethod newPaymentMethod = PaymentMethod.create(params, requestOptions);

            //Attach new payment method to customer
            Map<String, Object> payParams = new HashMap<String, Object>();
            payParams.put("customer", sharedCustomer.getId());
            newPaymentMethod.attach(payParams, requestOptions);

            //Setting the new method as the default payment method for the subscription
            Map<String, Object> customerUpdateParams = new HashMap<String, Object>();
            Map<String, Object> invoiceSettings = new HashMap<>();
            invoiceSettings.put("default_payment_method", newPaymentMethod.getId());
            customerUpdateParams.put("invoice_settings", invoiceSettings);
            sharedCustomer.update(customerUpdateParams, requestOptions);

            //Remove old payment method
            oldMethod.detach(requestOptions);

        } catch (StripeException e) {
            RestApiUtil.handleInternalServerError("Error while updating shared customer", e, log);
        }

    }

    /**
     * Returns the stripe key of the platform/tenant
     *
     * @param tenantId id of the tenant
     * @return the stripe key of the platform/tenant
     * @throws WorkflowException
     */
    private String getPlatformAccountKey(int tenantId, String keyType) throws APIManagementException {

        Object stripePlatformAccountKey = null;
        try {
            Registry configRegistry = ServiceReferenceHolder.getInstance().getRegistryService().getConfigSystemRegistry(
                    tenantId);
            if (configRegistry.resourceExists(APIConstants.API_TENANT_CONF_LOCATION)) {
                Resource resource = configRegistry.get(APIConstants.API_TENANT_CONF_LOCATION);
                String content = new String((byte[]) resource.getContent(), Charset.defaultCharset());

                if (StringUtils.isBlank(content)) {
                    String errorMessage = "Tenant configuration cannot be empty when configuring monetization.";
                    throw new APIManagementException(errorMessage);
                }
                //get the stripe key of patform account from tenant conf file
                JSONObject tenantConfig = (JSONObject) new JSONParser().parse(content);
                JSONObject monetizationInfo = (JSONObject) tenantConfig.get(
                        StripeMonetizationConstants.MONETIZATION_INFO);
                stripePlatformAccountKey = monetizationInfo.get(keyType);

                if (stripePlatformAccountKey != null && StringUtils.isBlank(stripePlatformAccountKey.toString())) {
                    throw new APIManagementException(keyType + " is empty!!!");
                }
            }
        } catch (RegistryException ex) {
            throw new APIManagementException("Could not get all registry objects : ", ex);
        } catch (org.json.simple.parser.ParseException ex) {
            throw new APIManagementException("Could not get Stripe Platform key : ", ex);
        }
        return stripePlatformAccountKey.toString();
    }

    /**
     * Get connected account key
     *
     * @param monetizationProperties
     * @return
     * @throws WorkflowException
     */
    private String getConnectedAccountKey(Map<String, String> monetizationProperties) throws APIManagementException {

        if (MapUtils.isNotEmpty(monetizationProperties) && monetizationProperties
                .containsKey(StripeMonetizationConstants.BILLING_ENGINE_CONNECTED_ACCOUNT_KEY)) {

            String connectedAccountKey = monetizationProperties
                    .get(StripeMonetizationConstants.BILLING_ENGINE_CONNECTED_ACCOUNT_KEY);

            if (StringUtils.isBlank(connectedAccountKey)) {
                String errorMessage = "Connected account stripe key was not found ";
                log.error(errorMessage);
                throw new APIManagementException(errorMessage);
            }
            return connectedAccountKey;
        } else {
            String errorMessage = "Stripe key of the connected account is empty.";
            log.error(errorMessage);
            throw new APIManagementException(errorMessage);
        }
    }
}
