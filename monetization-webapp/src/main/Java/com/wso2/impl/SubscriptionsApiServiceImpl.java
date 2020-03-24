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

import com.wso2.*;
import com.wso2.dto.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.MessageContext;

import com.wso2.dto.CompleteCheckoutRequestObjectDTO;
import com.wso2.dto.ErrorDTO;
import org.wso2.apim.monetization.impl.StripeMonetizationDAO;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowException;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutor;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowExecutorFactory;
import org.wso2.carbon.apimgt.impl.workflow.WorkflowStatus;
import org.wso2.carbon.apimgt.rest.api.util.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.util.HashMap;
import java.util.List;

import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public class SubscriptionsApiServiceImpl implements SubscriptionsApiService {

    private static final Log log = LogFactory.getLog(SubscriptionsApiServiceImpl.class);

    public Response subscriptionsCompleteCheckoutPost(CompleteCheckoutRequestObjectDTO body, MessageContext messageContext) {
        ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();

        if (body == null) {
            RestApiUtil.handleBadRequest("Request payload is missing", log);
        }

        String sessionId = body.getSessionId();
        String workflowReferenceId = body.getWorkflowReferenceId();

        if (workflowReferenceId == null) {
            RestApiUtil.handleBadRequest("workflowReferenceId is empty", log);
        }

        if (sessionId == null) {
            RestApiUtil.handleBadRequest("sessionId is empty", log);
        }

        try {
            WorkflowDTO workflowDTO = apiMgtDAO.retrieveWorkflow(workflowReferenceId);
            Map<String, String> attributes = new HashMap<>();
            attributes.put("sessionId",sessionId);
            workflowDTO.setAttributes(attributes);

            if (workflowDTO == null) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_WORKFLOW, workflowReferenceId, log);
            }

            workflowDTO.setStatus(WorkflowStatus.APPROVED);
            String workflowType = workflowDTO.getWorkflowType();
            WorkflowExecutor workflowExecutor = WorkflowExecutorFactory.getInstance().getWorkflowExecutor(workflowType);
            workflowExecutor.complete(workflowDTO);
            return Response.ok().build();
        } catch (APIManagementException e) {
            String msg = "Error while completing checkout ";
            RestApiUtil.handleInternalServerError(msg, e, log);
        } catch (WorkflowException e) {
            String msg = "Error while resuming workflow " + workflowReferenceId;
            RestApiUtil.handleInternalServerError(msg, e, log);
        }
        return null;
    }
}
