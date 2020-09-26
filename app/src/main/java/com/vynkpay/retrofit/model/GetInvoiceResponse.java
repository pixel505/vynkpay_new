package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetInvoiceResponse {

    @SerializedName("status")
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

        public List<Listing> getListing() {
            return listing;
        }

        public void setListing(List<Listing> listing) {
            this.listing = listing;
        }


        public class Listing {

            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("payment_via")
            @Expose
            private String paymentVia;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("invoice_number")
            @Expose
            private String invoiceNumber;
            @SerializedName("name")
            @Expose
            private String name;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPaymentVia() {
                return paymentVia;
            }

            public void setPaymentVia(String paymentVia) {
                this.paymentVia = paymentVia;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            public void setInvoiceNumber(String invoiceNumber) {
                this.invoiceNumber = invoiceNumber;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
        }

}
