package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetInvoiceDetailResponse {
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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("invoice_number")
        @Expose
        private String invoiceNumber;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("mode")
        @Expose
        private String mode;
        @SerializedName("tax")
        @Expose
        private String tax;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("address_line2")
        @Expose
        private String addressLine2;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("p_title")
        @Expose
        private String pTitle;
        @SerializedName("bit_address")
        @Expose
        private String bitAddress;
        @SerializedName("payment_via")
        @Expose
        private String paymentVia;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("paid_date")
        @Expose
        private String paidDate;
        @SerializedName("pp_price")
        @Expose
        private String ppPrice;
        @SerializedName("pp_gst")
        @Expose
        private String ppGst;
        @SerializedName("pp_total_price")
        @Expose
        private String ppTotalPrice;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile_code")
        @Expose
        private String mobileCode;
        @SerializedName("mobile")
        @Expose
        private String mobile;

        @SerializedName("pp_type")
        @Expose
        private String pp_type;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("total_coin")
        @Expose
        private String total_coin;

        public String getTotal_coin() {
            return total_coin;
        }

        public void setTotal_coin(String total_coin) {
            this.total_coin = total_coin;
        }

        public String getPp_type() {
            return pp_type;
        }

        public void setPp_type(String pp_type) {
            this.pp_type = pp_type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvoiceNumber() {
            return invoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPTitle() {
            return pTitle;
        }

        public void setPTitle(String pTitle) {
            this.pTitle = pTitle;
        }

        public String getBitAddress() {
            return bitAddress;
        }

        public void setBitAddress(String bitAddress) {
            this.bitAddress = bitAddress;
        }

        public String getPaymentVia() {
            return paymentVia;
        }

        public void setPaymentVia(String paymentVia) {
            this.paymentVia = paymentVia;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPaidDate() {
            return paidDate;
        }

        public void setPaidDate(String paidDate) {
            this.paidDate = paidDate;
        }

        public String getPpPrice() {
            return ppPrice;
        }

        public void setPpPrice(String ppPrice) {
            this.ppPrice = ppPrice;
        }

        public String getPpGst() {
            return ppGst;
        }

        public void setPpGst(String ppGst) {
            this.ppGst = ppGst;
        }

        public String getPpTotalPrice() {
            return ppTotalPrice;
        }

        public void setPpTotalPrice(String ppTotalPrice) {
            this.ppTotalPrice = ppTotalPrice;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileCode() {
            return mobileCode;
        }

        public void setMobileCode(String mobileCode) {
            this.mobileCode = mobileCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
    }
