package com.vynkpay.retrofit.model;

public class GetPackageDetailResponse {


    /**
     * status : true
     * data : {"image":"assets/images/phone-pe.png","name":"Send Phone Pe & Google Pay to","value":"9985061414","name2":"Send UPI to","value2":"vynkpay@upi","b_image":"assets/images/pnb.png","b_name":"Punjab National Bank","b_name_2":"Neu Vorieos LIfe (OPC) Pvt Ltd","account":"3972002100006352","IFSC":"PUNB0397200","address":"Saifabad, City : Hyderabad"}
     * message : Request payment Information.
     */

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
        /**
         * image : assets/images/phone-pe.png
         * name : Send Phone Pe & Google Pay to
         * value : 9985061414
         * name2 : Send UPI to
         * value2 : vynkpay@upi
         * b_image : assets/images/pnb.png
         * b_name : Punjab National Bank
         * b_name_2 : Neu Vorieos LIfe (OPC) Pvt Ltd
         * account : 3972002100006352
         * IFSC : PUNB0397200
         * address : Saifabad, City : Hyderabad
         */

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
