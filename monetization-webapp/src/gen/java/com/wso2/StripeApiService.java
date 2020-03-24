package com.wso2;

import com.wso2.*;
import com.wso2.dto.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.apimgt.api.APIManagementException;

import com.wso2.dto.ErrorDTO;
import com.wso2.dto.SetupCardSessionResponseDTO;
import com.wso2.dto.StripeCardDetailsResponseDTO;
import com.wso2.dto.UpdateStripeCardRequestObjectDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface StripeApiService {
      public Response stripeBillingDetailsGet(MessageContext messageContext) throws APIManagementException;
      public Response stripeSetupCardSessionApiIdPost(String apiId, MessageContext messageContext) throws APIManagementException;
      public Response stripeUpdateStripeCardPost(UpdateStripeCardRequestObjectDTO body, MessageContext messageContext) throws APIManagementException;
      public Response stripeWebhookWebhookKeyPost(String webhookKey, String body, MessageContext messageContext) throws APIManagementException;
}
