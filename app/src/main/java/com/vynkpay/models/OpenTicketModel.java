package com.vynkpay.models;

public class OpenTicketModel {
    private String tickedID;
    private String subject;
    private String description;
    private String department;
    private String date;
    private String status;

    public OpenTicketModel(String tickedID, String subject, String description, String department, String date, String status) {
        this.tickedID = tickedID;
        this.subject = subject;
        this.description = description;
        this.department = department;
        this.date = date;
        this.status = status;
    }

    public String getTickedID() {
        return tickedID;
    }

    public void setTickedID(String tickedID) {
        this.tickedID = tickedID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
