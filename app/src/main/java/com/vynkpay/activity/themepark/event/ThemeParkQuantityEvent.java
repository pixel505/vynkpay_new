package com.vynkpay.activity.themepark.event;

public class ThemeParkQuantityEvent {
    public String quantity;
    public float calculatedPrice;
    public float originalPrice;

    public ThemeParkQuantityEvent(String quantity, float calculatedPrice, float originalPrice) {
        this.quantity = quantity;
        this.calculatedPrice = calculatedPrice;
        this.originalPrice = originalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public float getCalculatedPrice() {
        return calculatedPrice;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }
}
