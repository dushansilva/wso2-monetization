package com.wso2;

import com.wso2.dto.CompleteCheckoutRequestObjectDTO;
import com.wso2.dto.ErrorDTO;
import com.wso2.SubscriptionsApiService;
import com.wso2.impl.SubscriptionsApiServiceImpl;
import org.wso2.carbon.apimgt.api.APIManagementException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.inject.Inject;

import io.swagger.annotations.*;
import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
@Path("/subscriptions")

@Api(description = "the subscriptions API")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public class SubscriptionsApi  {

  @Context MessageContext securityContext;

SubscriptionsApiService delegate = new SubscriptionsApiServiceImpl();


    @POST
    @Path("/complete-checkout")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Complete stripe checkout process ", notes = "The endpoint which is called to complete a monatized subscription process. Stripe checkout will redirect to the devportal and then this is called via devportal frontend. ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Stripe" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. ", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = ErrorDTO.class),
        @ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = ErrorDTO.class) })
    public Response subscriptionsCompleteCheckoutPost(@ApiParam(value = "Complete checkout object. " ,required=true) CompleteCheckoutRequestObjectDTO body) throws APIManagementException{
        return delegate.subscriptionsCompleteCheckoutPost(body, securityContext);
    }
}
