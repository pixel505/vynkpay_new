package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Club1Response {
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("listing")
        @Expose
        private List<Listing> listing = null;
        @SerializedName("page_title")
        @Expose
        private String pageTitle;
        @SerializedName("page_detail_url")
        @Expose
        private String pageDetailUrl;

        public List<Listing> getListing() {
            return listing;
        }

        public void setListing(List<Listing> listing) {
            this.listing = listing;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getPageDetailUrl() {
            return pageDetailUrl;
        }

        public void setPageDetailUrl(String pageDetailUrl) {
            this.pageDetailUrl = pageDetailUrl;
        }
    }
    public class Listing {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("club_slab_id")
        @Expose
        private String clubSlabId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("club")
        @Expose
        private String club;
        @SerializedName("slab")
        @Expose
        private String slab;
        @SerializedName("fix_amount")
        @Expose
        private String fixAmount;
        @SerializedName("other_detail")
        @Expose
        private String otherDetail;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("c_title")
        @Expose
        private String cTitle;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("full_name")
        @Expose
        private String fullName;
        @SerializedName("email")
        @Expose
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getClubSlabId() {
            return clubSlabId;
        }

        public void setClubSlabId(String clubSlabId) {
            this.clubSlabId = clubSlabId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClub() {
            return club;
        }

        public void setClub(String club) {
            this.club = club;
        }

        public String getSlab() {
            return slab;
        }

        public void setSlab(String slab) {
            this.slab = slab;
        }

        public String getFixAmount() {
            return fixAmount;
        }

        public void setFixAmount(String fixAmount) {
            this.fixAmount = fixAmount;
        }

        public String getOtherDetail() {
            return otherDetail;
        }

        public void setOtherDetail(String otherDetail) {
            this.otherDetail = otherDetail;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCTitle() {
            return cTitle;
        }

        public void setCTitle(String cTitle) {
            this.cTitle = cTitle;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
}
