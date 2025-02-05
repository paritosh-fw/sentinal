package com.hackathon.anomaly.detector.model;


public class AnomalyDetectionResponse {

    private String response;

    // Default constructor
    public AnomalyDetectionResponse() {}

    // Constructor to initialize response
    public AnomalyDetectionResponse(String response) {
        this.response = response;
    }

    // Getter for response
    public String getResponse() {
        return response;
    }

    // Setter for response
    public void setResponse(String response) {
        this.response = response;
    }
}
