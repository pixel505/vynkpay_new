package com.vynkpay.activity.themepark.event;

public class IsTicketTypeSelectedEvent {
    public boolean isTicketTypeSelected;

    public boolean isTicketTypeSelected() {
        return isTicketTypeSelected;
    }

    public IsTicketTypeSelectedEvent(boolean isTicketTypeSelected) {
        this.isTicketTypeSelected = isTicketTypeSelected;
    }
}
