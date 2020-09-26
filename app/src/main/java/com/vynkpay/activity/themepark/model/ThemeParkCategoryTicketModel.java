package com.vynkpay.activity.themepark.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ThemeParkCategoryTicketModel implements Parcelable{

    private String ticketCategoryName;
    private String tiketCategoryId;
    private String tiketCategoryMin;
    private String tiketCategoryMax;
    private String tiketCategoryMaxAge;
    private String tiketCategoryMinAge;
    private String tiketCategoryPrice;


    public ThemeParkCategoryTicketModel() {
    }

    protected ThemeParkCategoryTicketModel(Parcel in) {
        ticketCategoryName = in.readString();
        tiketCategoryId = in.readString();
        tiketCategoryMin = in.readString();
        tiketCategoryMax = in.readString();
        tiketCategoryMaxAge = in.readString();
        tiketCategoryMinAge = in.readString();
        tiketCategoryPrice = in.readString();
    }

    public static final Creator<ThemeParkCategoryTicketModel> CREATOR = new Creator<ThemeParkCategoryTicketModel>() {
        @Override
        public ThemeParkCategoryTicketModel createFromParcel(Parcel in) {
            return new ThemeParkCategoryTicketModel(in);
        }

        @Override
        public ThemeParkCategoryTicketModel[] newArray(int size) {
            return new ThemeParkCategoryTicketModel[size];
        }
    };

    public String getTiketCategoryMaxAge() {
        return tiketCategoryMaxAge;
    }

    public void setTiketCategoryMaxAge(String tiketCategoryMaxAge) {
        this.tiketCategoryMaxAge = tiketCategoryMaxAge;
    }

    public String getTiketCategoryMinAge() {
        return tiketCategoryMinAge;
    }

    public void setTiketCategoryMinAge(String tiketCategoryMinAge) {
        this.tiketCategoryMinAge = tiketCategoryMinAge;
    }



    public String getTicketCategoryName() {
        return ticketCategoryName;
    }

    public void setTicketCategoryName(String ticketCategoryName) {
        this.ticketCategoryName = ticketCategoryName;
    }

    public String getTiketCategoryId() {
        return tiketCategoryId;
    }

    public void setTiketCategoryId(String tiketCategoryId) {
        this.tiketCategoryId = tiketCategoryId;
    }

    public String getTiketCategoryMin() {
        return tiketCategoryMin;
    }

    public void setTiketCategoryMin(String tiketCategoryMin) {
        this.tiketCategoryMin = tiketCategoryMin;
    }

    public String getTiketCategoryMax() {
        return tiketCategoryMax;
    }

    public void setTiketCategoryMax(String tiketCategoryMax) {
        this.tiketCategoryMax = tiketCategoryMax;
    }

    public String getTiketCategoryPrice() {
        return tiketCategoryPrice;
    }

    public void setTiketCategoryPrice(String tiketCategoryPrice) {
        this.tiketCategoryPrice = tiketCategoryPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticketCategoryName);
        dest.writeString(tiketCategoryId);
        dest.writeString(tiketCategoryMin);
        dest.writeString(tiketCategoryMax);
        dest.writeString(tiketCategoryMaxAge);
        dest.writeString(tiketCategoryMinAge);
        dest.writeString(tiketCategoryPrice);
    }
}
