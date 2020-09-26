package com.vynkpay.events;

public class UpDateUIEvent {
    public boolean isUpdated;

    public UpDateUIEvent(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }
}
