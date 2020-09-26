package com.vynkpay.activity.themepark.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ThemeParkTicketModel implements Parcelable {
    private String id;
    private String title;
    private String visitDate;
    private String categoryRawString;
    private ArrayList<ThemeParkCategoryTicketModel> subCategoryData;

    public ThemeParkTicketModel() {
    }

    protected ThemeParkTicketModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        visitDate = in.readString();
        categoryRawString = in.readString();
        subCategoryData = in.createTypedArrayList(ThemeParkCategoryTicketModel.CREATOR);
    }

    public static final Creator<ThemeParkTicketModel> CREATOR = new Creator<ThemeParkTicketModel>() {
        @Override
        public ThemeParkTicketModel createFromParcel(Parcel in) {
            return new ThemeParkTicketModel(in);
        }

        @Override
        public ThemeParkTicketModel[] newArray(int size) {
            return new ThemeParkTicketModel[size];
        }
    };

    public ArrayList<ThemeParkCategoryTicketModel> getSubCategoryData() {
        return subCategoryData;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public void setSubCategoryData(ArrayList<ThemeParkCategoryTicketModel> subCategoryData) {
        this.subCategoryData = subCategoryData;
    }

    public String getCategoryRawString() {
        return categoryRawString;
    }

    public void setCategoryRawString(String categoryRawString) {
        this.categoryRawString = categoryRawString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(visitDate);
        dest.writeString(categoryRawString);
        dest.writeTypedList(subCategoryData);
    }
}
