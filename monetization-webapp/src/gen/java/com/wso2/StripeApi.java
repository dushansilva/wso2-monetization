package com.wso2;

import com.wso2.dto.ErrorDTO;
import com.wso2.dto.SetupCardSessionResponseDTO;
import com.wso2.dto.StripeCardDetailsResponseDTO;
import com.wso2.dto.UpdateStripeCardRequestObjectDTO;
import com.wso2.StripeApiService;
import com.wso2.impl.StripeApiServiceImpl;
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
@Path("/stripe")

@Api(description = "the stripe API")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public class StripeApi  {

  @Context MessageContext securityContext;

StripeApiService delegate = new StripeApiServiceImpl();


    @GET
    @Path("/billing-details")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "get stripe billing details ", notes = "The endpoint which is called to get card/billing details ", response = StripeCardDetailsResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Stripe",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. ", response = StripeCardDetailsResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = ErrorDTO.class),
        @ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = ErrorDTO.class) })
    public Response stripeBillingDetailsGet() throws APIManagementException{
        return delegate.stripeBillingDetailsGet(securityContext);
    }

    @POST
    @Path("/setup-card-session/{apiId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Start card updating session ", notes = "The endpoint which is called to start update a the card details session ", response = SetupCardSessionResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Stripe",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. ", response = SetupCardSessionResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = ErrorDTO.class),
        @ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = ErrorDTO.class) })
    public Response stripeSetupCardSessionApiIdPost(@ApiParam(value = "**API ID** consisting of the **UUID** of the API. ",required=true) @PathParam("apiId") String apiId) throws APIManagementException{
        return delegate.stripeSetupCardSessionApiIdPost(apiId, securityContext);
    }

    @POST
    @Path("/update-stripe-card")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update customer card details ", notes = "The endpoint which is called to update a the card details of a customer ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Stripe",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. ", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = ErrorDTO.class),
        @ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = ErrorDTO.class) })
    public Response stripeUpdateStripeCardPost(@ApiParam(value = "Complete checkout object. " ,required=true) UpdateStripeCardRequestObjectDTO body) throws APIManagementException{
        return delegate.stripeUpdateStripeCardPost(body, securityContext);
    }

    @POST
    @Path("/webhook/{webhookKey}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "get stripe billing details ", notes = "The endpoint which is called to get card/billing details ", response = StripeCardDetailsResponseDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            
        })
    }, tags={ "Stripe" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. ", response = StripeCardDetailsResponseDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = ErrorDTO.class),
        @ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = ErrorDTO.class) })
    public Response stripeWebhookWebhookKeyPost(@ApiParam(value = "webhook key corresponding to key entered in tenant-conf.json ",required=true) @PathParam("webhookKey") String webhookKey, @ApiParam(value = "webhook. " ) String body) throws APIManagementException{
        return delegate.stripeWebhookWebhookKeyPost(webhookKey, body, securityContext);
    }
}
