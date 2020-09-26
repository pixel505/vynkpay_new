package com.vynkpay.activity.themepark.event;

import com.vynkpay.activity.themepark.model.ThemeParkTicketModel;

public class ThemeParkTicketEvent {
    public ThemeParkTicketModel themeParkTicketModel;

    public ThemeParkTicketEvent(ThemeParkTicketModel themeParkTicketModel) {
        this.themeParkTicketModel = themeParkTicketModel;
    }

    public ThemeParkTicketModel getThemeParkTicketModel() {
        return themeParkTicketModel;
    }
}
