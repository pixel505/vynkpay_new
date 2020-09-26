package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetChatResponse {

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

        @SerializedName("ticket")
        @Expose
        private Ticket ticket;
        @SerializedName("conversation")
        @Expose
        private List<Conversation> conversation = null;

        public Ticket getTicket() {
            return ticket;
        }

        public void setTicket(Ticket ticket) {
            this.ticket = ticket;
        }

        public List<Conversation> getConversation() {
            return conversation;
        }

        public void setConversation(List<Conversation> conversation) {
            this.conversation = conversation;
        }


        public class Conversation {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("body")
            @Expose
            private String body;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("usertype")
            @Expose
            private String usertype;
            @SerializedName("image")
            @Expose
            private Object image;
            @SerializedName("attachment")
            @Expose
            private Object attachment;
            @SerializedName("path")
            @Expose
            private Object path;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public Object getImage() {
                return image;
            }

            public void setImage(Object image) {
                this.image = image;
            }

            public Object getAttachment() {
                return attachment;
            }

            public void setAttachment(Object attachment) {
                this.attachment = attachment;
            }

            public Object getPath() {
                return path;
            }

            public void setPath(Object path) {
                this.path = path;
            }
        }


        public class Ticket {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("subject")
            @Expose
            private String subject;
            @SerializedName("body")
            @Expose
            private String body;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("modified_date")
            @Expose
            private String modifiedDate;
            @SerializedName("isactive")
            @Expose
            private String isactive;
            @SerializedName("ticket_no")
            @Expose
            private String ticketNo;
            @SerializedName("image")
            @Expose
            private String image;
            @SerializedName("path")
            @Expose
            private String path;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("attachment")
            @Expose
            private String attachment;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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

            public String getTicketNo() {
                return ticketNo;
            }

            public void setTicketNo(String ticketNo) {
                this.ticketNo = ticketNo;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAttachment() {
                return attachment;
            }

            public void setAttachment(String attachment) {
                this.attachment = attachment;
            }
        }
        }
}
