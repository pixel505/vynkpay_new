package com.vynkpay.events;

public class DmtRegisterEvent {
    public String isRgistered;
    public boolean isChangeLayout;

    public DmtRegisterEvent(String remitterId, boolean isChangeLayout) {
        this.isRgistered = remitterId;
        this.isChangeLayout = isChangeLayout;
    }

    public String getRemitterId() {
        return isRgistered;
    }

    public boolean isChangeLayout() {
        return isChangeLayout;
    }
}
