package com.vynkpay.retrofit.model;

import java.util.List;

public class NotificationResponse {


    /**
     * success : true
     * data : {"notification":[{"id":"8","user_id":"0","subject":"test","body":"test img","create_date":"2020-08-27 17:31:04","image":"https://www.mlm.pixelsoftwares.com/vynkpay/account/uploads/notification/Not5f47a080a53791598529664.jpg","read_status":"0"},{"id":"6","user_id":"0","subject":"Withdrawal for Indian Affiliates","body":"Dear Affiliates, we have just been informed of a maintenance activity at the NPCI's end, which will begin on Aug 14, 2020 between 02:00 Hrs to 02:45 Hrs. Request you to hold withdrawals  during this timeline. Regret the inconvenience. - Team VynkPay ","create_date":"2020-08-14 02:27:53","image":null,"read_status":"1"}],"unread_count":"1"}
     * message : Success
     */

    private boolean success;
    private DataBean data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
         * notification : [{"id":"8","user_id":"0","subject":"test","body":"test img","create_date":"2020-08-27 17:31:04","image":"https://www.mlm.pixelsoftwares.com/vynkpay/account/uploads/notification/Not5f47a080a53791598529664.jpg","read_status":"0"},{"id":"6","user_id":"0","subject":"Withdrawal for Indian Affiliates","body":"Dear Affiliates, we have just been informed of a maintenance activity at the NPCI's end, which will begin on Aug 14, 2020 between 02:00 Hrs to 02:45 Hrs. Request you to hold withdrawals  during this timeline. Regret the inconvenience. - Team VynkPay ","create_date":"2020-08-14 02:27:53","image":null,"read_status":"1"}]
         * unread_count : 1
         */

        private String unread_count;
        private List<NotificationBean> notification;

        public String getUnread_count() {
            return unread_count;
        }

        public void setUnread_count(String unread_count) {
            this.unread_count = unread_count;
        }

        public List<NotificationBean> getNotification() {
            return notification;
        }

        public void setNotification(List<NotificationBean> notification) {
            this.notification = notification;
        }

        public static class NotificationBean {
            /**
             * id : 8
             * user_id : 0
             * subject : test
             * body : test img
             * create_date : 2020-08-27 17:31:04
             * image : https://www.mlm.pixelsoftwares.com/vynkpay/account/uploads/notification/Not5f47a080a53791598529664.jpg
             * read_status : 0
             */

            private String id;
            private String user_id;
            private String subject;
            private String body;
            private String create_date;
            private String image;
            private String read_status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getRead_status() {
                return read_status;
            }

            public void setRead_status(String read_status) {
                this.read_status = read_status;
            }
        }
    }
}
