package com.vynkpay.retrofit.model;

public class GetPackageDetailResponse {

    private String status;
    private DataBean data;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

        private String image;
        private String name;
        private String value;
        private String name2;
        private String value2;
        private String b_image;
        private String b_name;
        private String b_name_2;
        private String account;
        private String IFSC;
        private String address;

        private String wallet_enable;
        private String bank_enable;
        private String trx_address_enable;
        private String trx_address_image;
        private String trx_address;
        private String payer_enable;
        private String payer_image;
        private String payer_account;


        public String getWallet_enable() {
            return wallet_enable;
        }

        public void setWallet_enable(String wallet_enable) {
            this.wallet_enable = wallet_enable;
        }

        public String getBank_enable() {
            return bank_enable;
        }

        public void setBank_enable(String bank_enable) {
            this.bank_enable = bank_enable;
        }

        public String getTrx_address_enable() {
            return trx_address_enable;
        }

        public void setTrx_address_enable(String trx_address_enable) {
            this.trx_address_enable = trx_address_enable;
        }

        public String getTrx_address_image() {
            return trx_address_image;
        }

        public void setTrx_address_image(String trx_address_image) {
            this.trx_address_image = trx_address_image;
        }

        public String getTrx_address() {
            return trx_address;
        }

        public void setTrx_address(String trx_address) {
            this.trx_address = trx_address;
        }

        public String getPayer_enable() {
            return payer_enable;
        }

        public void setPayer_enable(String payer_enable) {
            this.payer_enable = payer_enable;
        }

        public String getPayer_image() {
            return payer_image;
        }

        public void setPayer_image(String payer_image) {
            this.payer_image = payer_image;
        }

        public String getPayer_account() {
            return payer_account;
        }

        public void setPayer_account(String payer_account) {
            this.payer_account = payer_account;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getB_image() {
            return b_image;
        }

        public void setB_image(String b_image) {
            this.b_image = b_image;
        }

        public String getB_name() {
            return b_name;
        }

        public void setB_name(String b_name) {
            this.b_name = b_name;
        }

        public String getB_name_2() {
            return b_name_2;
        }

        public void setB_name_2(String b_name_2) {
            this.b_name_2 = b_name_2;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getIFSC() {
            return IFSC;
        }

        public void setIFSC(String IFSC) {
            this.IFSC = IFSC;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
