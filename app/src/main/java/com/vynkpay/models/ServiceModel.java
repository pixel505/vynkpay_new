package com.vynkpay.models;

public class ServiceModel {
    private String serviceName;
    private String serviceIcon;
    private int serviceIconInt;

    public ServiceModel(String serviceName, int serviceIconInt) {
        this.serviceName = serviceName;
        this.serviceIconInt = serviceIconInt;
    }

    public int getServiceIconInt() {
        return serviceIconInt;
    }

    public void setServiceIconInt(int serviceIconInt) {
        this.serviceIconInt = serviceIconInt;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon) {
        this.serviceIcon = serviceIcon;
    }
}
