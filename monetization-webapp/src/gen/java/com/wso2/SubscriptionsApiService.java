package com.wso2;

import com.wso2.*;
import com.wso2.dto.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.apimgt.api.APIManagementException;

import com.wso2.dto.CompleteCheckoutRequestObjectDTO;
import com.wso2.dto.ErrorDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface SubscriptionsApiService {
      public Response subscriptionsCompleteCheckoutPost(CompleteCheckoutRequestObjectDTO body, MessageContext messageContext) throws APIManagementException;
}
