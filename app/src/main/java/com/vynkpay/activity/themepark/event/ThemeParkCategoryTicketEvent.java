package com.vynkpay.activity.themepark.event;

import com.vynkpay.activity.themepark.model.ThemeParkCategoryTicketModel;

public class ThemeParkCategoryTicketEvent {
    public ThemeParkCategoryTicketModel themeParkTicketModel;

    public ThemeParkCategoryTicketEvent(ThemeParkCategoryTicketModel themeParkTicketModel) {
        this.themeParkTicketModel = themeParkTicketModel;
    }

    public ThemeParkCategoryTicketModel getThemeParkTicketModel() {
        return themeParkTicketModel;
    }
}
