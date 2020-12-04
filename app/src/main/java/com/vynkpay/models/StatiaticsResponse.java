package com.vynkpay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatiaticsResponse {

    @SerializedName("des_title")
    @Expose
    private String desTitle;
    @SerializedName("des_img")
    @Expose
    private String desImg;
    @SerializedName("next_des_title")
    @Expose
    private String nextDesTitle;
    @SerializedName("next_des_img")
    @Expose
    private String nextDesImg;
    @SerializedName("token_des_title")
    @Expose
    private String tokenDesTitle;
    @SerializedName("token_des_img")
    @Expose
    private String tokenDesImg;
    @SerializedName("next_token_des_title")
    @Expose
    private String nextTokenDesTitle;
    @SerializedName("next_token_des_img")
    @Expose
    private String nextTokenDesImg;
    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;
    @SerializedName("purchase_amount")
    @Expose
    private String purchaseAmount;
    @SerializedName("total_earning")
    @Expose
    private String totalEarning;


    public StatiaticsResponse(String desTitle, String desImg, String nextDesTitle, String nextDesImg, String tokenDesTitle, String tokenDesImg, String nextTokenDesTitle, String nextTokenDesImg, String purchaseDate, String purchaseAmount, String totalEarning) {
        this.desTitle = desTitle;
        this.desImg = desImg;
        this.nextDesTitle = nextDesTitle;
        this.nextDesImg = nextDesImg;
        this.tokenDesTitle = tokenDesTitle;
        this.tokenDesImg = tokenDesImg;
        this.nextTokenDesTitle = nextTokenDesTitle;
        this.nextTokenDesImg = nextTokenDesImg;
        this.purchaseDate = purchaseDate;
        this.purchaseAmount = purchaseAmount;
        this.totalEarning = totalEarning;
    }

    public String getDesTitle() {
        return desTitle;
    }

    public void setDesTitle(String desTitle) {
        this.desTitle = desTitle;
    }

    public String getDesImg() {
        return desImg;
    }

    public void setDesImg(String desImg) {
        this.desImg = desImg;
    }

    public String getNextDesTitle() {
        return nextDesTitle;
    }

    public void setNextDesTitle(String nextDesTitle) {
        this.nextDesTitle = nextDesTitle;
    }

    public String getNextDesImg() {
        return nextDesImg;
    }

    public void setNextDesImg(String nextDesImg) {
        this.nextDesImg = nextDesImg;
    }

    public String getTokenDesTitle() {
        return tokenDesTitle;
    }

    public void setTokenDesTitle(String tokenDesTitle) {
        this.tokenDesTitle = tokenDesTitle;
    }

    public String getTokenDesImg() {
        return tokenDesImg;
    }

    public void setTokenDesImg(String tokenDesImg) {
        this.tokenDesImg = tokenDesImg;
    }

    public String getNextTokenDesTitle() {
        return nextTokenDesTitle;
    }

    public void setNextTokenDesTitle(String nextTokenDesTitle) {
        this.nextTokenDesTitle = nextTokenDesTitle;
    }

    public String getNextTokenDesImg() {
        return nextTokenDesImg;
    }

    public void setNextTokenDesImg(String nextTokenDesImg) {
        this.nextTokenDesImg = nextTokenDesImg;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(String totalEarning) {
        this.totalEarning = totalEarning;
    }

}
