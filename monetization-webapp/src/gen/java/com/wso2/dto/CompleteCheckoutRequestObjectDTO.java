package com.wso2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.util.annotations.Scope;



public class CompleteCheckoutRequestObjectDTO   {
  
    private String sessionId = null;
    private String workflowReferenceId = null;

  /**
   * sessionId of session created in stripe
   **/
  public CompleteCheckoutRequestObjectDTO sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "sessionId of session created in stripe")
  @JsonProperty("sessionId")
  @NotNull
  public String getSessionId() {
    return sessionId;
  }
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * workflow external reference id stored to indentify a workflow
   **/
  public CompleteCheckoutRequestObjectDTO workflowReferenceId(String workflowReferenceId) {
    this.workflowReferenceId = workflowReferenceId;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "workflow external reference id stored to indentify a workflow")
  @JsonProperty("workflowReferenceId")
  @NotNull
  public String getWorkflowReferenceId() {
    return workflowReferenceId;
  }
  public void setWorkflowReferenceId(String workflowReferenceId) {
    this.workflowReferenceId = workflowReferenceId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteCheckoutRequestObjectDTO completeCheckoutRequestObject = (CompleteCheckoutRequestObjectDTO) o;
    return Objects.equals(sessionId, completeCheckoutRequestObject.sessionId) &&
        Objects.equals(workflowReferenceId, completeCheckoutRequestObject.workflowReferenceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, workflowReferenceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteCheckoutRequestObjectDTO {\n");
    
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    workflowReferenceId: ").append(toIndentedString(workflowReferenceId)).append("\n");
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

