package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOpenCloseTicket {

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

        @SerializedName("tickets")
        @Expose
        private Integer tickets;
        @SerializedName("close_tickets")
        @Expose
        private List<CloseTicket> closeTickets = null;
        @SerializedName("actv_tickets")
        @Expose
        private List<ActvTicket> actvTickets = null;
        @SerializedName("active_tickets")
        @Expose
        private Integer activeTickets;

        public Integer getTickets() {
            return tickets;
        }

        public void setTickets(Integer tickets) {
            this.tickets = tickets;
        }

        public List<CloseTicket> getCloseTickets() {
            return closeTickets;
        }

        public void setCloseTickets(List<CloseTicket> closeTickets) {
            this.closeTickets = closeTickets;
        }

        public List<ActvTicket> getActvTickets() {
            return actvTickets;
        }

        public void setActvTickets(List<ActvTicket> actvTickets) {
            this.actvTickets = actvTickets;
        }

        public Integer getActiveTickets() {
            return activeTickets;
        }

        public void setActiveTickets(Integer activeTickets) {
            this.activeTickets = activeTickets;
        }

        public class CloseTicket {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("subject")
            @Expose
            private String subject;
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
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("msg")
            @Expose
            private String msg;
            @SerializedName("dept")
            @Expose
            private String dept;
            @SerializedName("phone")
            @Expose
            private String phone;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getDept() {
                return dept;
            }

            public void setDept(String dept) {
                this.dept = dept;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

        }

        public class ActvTicket {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("subject")
            @Expose
            private String subject;
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
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("msg")
            @Expose
            private String msg;
            @SerializedName("dept")
            @Expose
            private String dept;
            @SerializedName("phone")
            @Expose
            private String phone;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getDept() {
                return dept;
            }

            public void setDept(String dept) {
                this.dept = dept;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }

    }
