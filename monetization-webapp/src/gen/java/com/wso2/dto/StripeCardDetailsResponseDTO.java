package com.wso2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.util.annotations.Scope;



public class StripeCardDetailsResponseDTO   {
  
    private String brand = null;
    private BigDecimal expMonth = null;
    private BigDecimal expYear = null;
    private String last4 = null;

  /**
   * card type
   **/
  public StripeCardDetailsResponseDTO brand(String brand) {
    this.brand = brand;
    return this;
  }

  
  @ApiModelProperty(value = "card type")
  @JsonProperty("brand")
  public String getBrand() {
    return brand;
  }
  public void setBrand(String brand) {
    this.brand = brand;
  }

  /**
   * expiry month
   **/
  public StripeCardDetailsResponseDTO expMonth(BigDecimal expMonth) {
    this.expMonth = expMonth;
    return this;
  }

  
  @ApiModelProperty(value = "expiry month")
  @JsonProperty("expMonth")
  public BigDecimal getExpMonth() {
    return expMonth;
  }
  public void setExpMonth(BigDecimal expMonth) {
    this.expMonth = expMonth;
  }

  /**
   * expiry year
   **/
  public StripeCardDetailsResponseDTO expYear(BigDecimal expYear) {
    this.expYear = expYear;
    return this;
  }

  
  @ApiModelProperty(value = "expiry year")
  @JsonProperty("expYear")
  public BigDecimal getExpYear() {
    return expYear;
  }
  public void setExpYear(BigDecimal expYear) {
    this.expYear = expYear;
  }

  /**
   * last 4 digits of the card
   **/
  public StripeCardDetailsResponseDTO last4(String last4) {
    this.last4 = last4;
    return this;
  }

  
  @ApiModelProperty(value = "last 4 digits of the card")
  @JsonProperty("last4")
  public String getLast4() {
    return last4;
  }
  public void setLast4(String last4) {
    this.last4 = last4;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StripeCardDetailsResponseDTO stripeCardDetailsResponse = (StripeCardDetailsResponseDTO) o;
    return Objects.equals(brand, stripeCardDetailsResponse.brand) &&
        Objects.equals(expMonth, stripeCardDetailsResponse.expMonth) &&
        Objects.equals(expYear, stripeCardDetailsResponse.expYear) &&
        Objects.equals(last4, stripeCardDetailsResponse.last4);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brand, expMonth, expYear, last4);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StripeCardDetailsResponseDTO {\n");
    
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    expMonth: ").append(toIndentedString(expMonth)).append("\n");
    sb.append("    expYear: ").append(toIndentedString(expYear)).append("\n");
    sb.append("    last4: ").append(toIndentedString(last4)).append("\n");
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

