package com.vynkpay.models;

public class OfferModel {
    private String title;
    private String description;
    private String image;
    private String offerCode;
    private String offerCodeText;

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferCodeText() {
        return offerCodeText;
    }

    public void setOfferCodeText(String offerCodeText) {
        this.offerCodeText = offerCodeText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
