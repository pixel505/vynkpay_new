package com.vynkpay.events;

public class UpdateDmtFields {
    public boolean isDeleted;

    public UpdateDmtFields(boolean isDeleted) {
        this.isDeleted = isDeleted;;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
