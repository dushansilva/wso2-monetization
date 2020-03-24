package com.wso2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.util.annotations.Scope;



public class SetupCardSessionResponseDTO   {
  
    private String sessionId = null;
    private String stripePublishableKey = null;

  /**
   * sessionId of session created in stripe
   **/
  public SetupCardSessionResponseDTO sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  
  @ApiModelProperty(value = "sessionId of session created in stripe")
  @JsonProperty("sessionId")
  public String getSessionId() {
    return sessionId;
  }
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * publishable key of the platform account
   **/
  public SetupCardSessionResponseDTO stripePublishableKey(String stripePublishableKey) {
    this.stripePublishableKey = stripePublishableKey;
    return this;
  }

  
  @ApiModelProperty(value = "publishable key of the platform account")
  @JsonProperty("stripePublishableKey")
  public String getStripePublishableKey() {
    return stripePublishableKey;
  }
  public void setStripePublishableKey(String stripePublishableKey) {
    this.stripePublishableKey = stripePublishableKey;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetupCardSessionResponseDTO setupCardSessionResponse = (SetupCardSessionResponseDTO) o;
    return Objects.equals(sessionId, setupCardSessionResponse.sessionId) &&
        Objects.equals(stripePublishableKey, setupCardSessionResponse.stripePublishableKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, stripePublishableKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetupCardSessionResponseDTO {\n");
    
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    stripePublishableKey: ").append(toIndentedString(stripePublishableKey)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

