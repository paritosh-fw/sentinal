package com.hackathon.anomaly.detector.service;

import com.hackathon.anomaly.detector.model.AnomalyDetectionResponse;
import com.hackathon.anomaly.detector.model.SendEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyCustomerIfAnomaly(String anomalyResponse) {
        String notificationUrl = "http://localhost:8081/api/notify";  // Your Notification Microservice URL

        // Send the anomaly response to the notification service
        SendEmailRequest sendEmailRequest = new SendEmailRequest(anomalyResponse, "Robert", "paritosh.agarwal@freshworks.com", "User Creation");
        restTemplate.postForObject(notificationUrl, sendEmailRequest, String.class);
    }
}
