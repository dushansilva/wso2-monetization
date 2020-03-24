package com.wso2.dto;

public class CardUpdateDetailsDTO {
    private int subscribedApiId;
    private int subscriberId;
    private String sharedCustomerId;
    private String parentCustomerId;

    public int getSubscribedApiId() {
        return subscribedApiId;
    }

    public void setSubscribedApiId(int subscribedApiId) {
        this.subscribedApiId = subscribedApiId;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSharedCustomerId() {
        return sharedCustomerId;
    }

    public void setSharedCustomerId(String sharedCustomerId) {
        this.sharedCustomerId = sharedCustomerId;
    }

    public String getParentCustomerId() {
        return parentCustomerId;
    }

    public void setParentCustomerId(String parentCustomerId) {
        this.parentCustomerId = parentCustomerId;
    }
}
