package com.hackathon.anomaly.detector.model;


public class SendEmailRequest {

    private String message;

    public SendEmailRequest(String message, String username, String userEmailAddress, String action) {
        this.message = message;
        this.username = username;
        this.userEmailAddress = userEmailAddress;
        this.action = action;
    }

    private String username;
    private String userEmailAddress;
    private String action;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Default constructor
    public SendEmailRequest() {}
}
