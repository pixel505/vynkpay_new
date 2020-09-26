package com.vynkpay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EcashModelClass {

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

        @SerializedName("ecash")
        @Expose
        private List<Ecash> ecash = null;

        public List<Ecash> getEcash() {
            return ecash;
        }

        public void setEcash(List<Ecash> ecash) {
            this.ecash = ecash;
        }

        public class Ecash {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("image")
            @Expose
            private Object image;
            @SerializedName("image_thumb")
            @Expose
            private Object imageThumb;
            @SerializedName("path")
            @Expose
            private Object path;
            @SerializedName("detosited_at")
            @Expose
            private String detositedAt;
            @SerializedName("txn_no")
            @Expose
            private String txnNo;
            @SerializedName("company_account")
            @Expose
            private String companyAccount;
            @SerializedName("card_type")
            @Expose
            private String cardType;
            @SerializedName("invoice_number")
            @Expose
            private String invoiceNumber;
            @SerializedName("issued_by")
            @Expose
            private String issuedBy;
            @SerializedName("swapped_by")
            @Expose
            private String swappedBy;
            @SerializedName("transaction_date")
            @Expose
            private Object transactionDate;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("remarks")
            @Expose
            private String remarks;
            @SerializedName("description")
            @Expose
            private Object description;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("modified_date")
            @Expose
            private String modifiedDate;
            @SerializedName("isactive")
            @Expose
            private String isactive;

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

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public Object getImage() {
                return image;
            }

            public void setImage(Object image) {
                this.image = image;
            }

            public Object getImageThumb() {
                return imageThumb;
            }

            public void setImageThumb(Object imageThumb) {
                this.imageThumb = imageThumb;
            }

            public Object getPath() {
                return path;
            }

            public void setPath(Object path) {
                this.path = path;
            }

            public String getDetositedAt() {
                return detositedAt;
            }

            public void setDetositedAt(String detositedAt) {
                this.detositedAt = detositedAt;
            }

            public String getTxnNo() {
                return txnNo;
            }

            public void setTxnNo(String txnNo) {
                this.txnNo = txnNo;
            }

            public String getCompanyAccount() {
                return companyAccount;
            }

            public void setCompanyAccount(String companyAccount) {
                this.companyAccount = companyAccount;
            }

            public String getCardType() {
                return cardType;
            }

            public void setCardType(String cardType) {
                this.cardType = cardType;
            }

            public String getInvoiceNumber() {
                return invoiceNumber;
            }

            public void setInvoiceNumber(String invoiceNumber) {
                this.invoiceNumber = invoiceNumber;
            }

            public String getIssuedBy() {
                return issuedBy;
            }

            public void setIssuedBy(String issuedBy) {
                this.issuedBy = issuedBy;
            }

            public String getSwappedBy() {
                return swappedBy;
            }

            public void setSwappedBy(String swappedBy) {
                this.swappedBy = swappedBy;
            }

            public Object getTransactionDate() {
                return transactionDate;
            }

            public void setTransactionDate(Object transactionDate) {
                this.transactionDate = transactionDate;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getModifiedDate() {
                return modifiedDate;
            }

            public void setModifiedDate(String modifiedDate) {
                this.modifiedDate = modifiedDate;
            }

            public String getIsactive() {
                return isactive;
            }

            public void setIsactive(String isactive) {
                this.isactive = isactive;
            }
        }
    }
}
