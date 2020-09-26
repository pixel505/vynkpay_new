package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationReadResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

        @SerializedName("notification")
        @Expose
        private List<Notification> notification = null;
        @SerializedName("unread_count")
        @Expose
        private String unreadCount;

        public List<Notification> getNotification() {
            return notification;
        }

        public void setNotification(List<Notification> notification) {
            this.notification = notification;
        }

        public String getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(String unreadCount) {
            this.unreadCount = unreadCount;
        }


        public class Notification {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("subject")
            @Expose
            private String subject;
            @SerializedName("body")
            @Expose
            private String body;
            @SerializedName("create_date")
            @Expose
            private String createDate;
            @SerializedName("image")
            @Expose
            private Object image;
            @SerializedName("read_status")
            @Expose
            private String readStatus;

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

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public Object getImage() {
                return image;
            }

            public void setImage(Object image) {
                this.image = image;
            }

            public String getReadStatus() {
                return readStatus;
            }

            public void setReadStatus(String readStatus) {
                this.readStatus = readStatus;
            }
        }
        }
}
