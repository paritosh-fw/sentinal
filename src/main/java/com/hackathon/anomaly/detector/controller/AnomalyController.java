package com.hackathon.anomaly.detector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.anomaly.detector.model.AnomalyDetectRequest;
import com.hackathon.anomaly.detector.model.Event;
import com.hackathon.anomaly.detector.repository.EventRepository;
import com.hackathon.anomaly.detector.service.AnomalyDetectionService;
import com.hackathon.anomaly.detector.service.LLMService;
import com.hackathon.anomaly.detector.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anomalies")
public class AnomalyController {
    private final EventRepository eventRepository;
    private final AnomalyDetectionService anomalyDetectionService;
    private final LLMService llmService;
    private final NotificationService notificationService;

    public AnomalyController(EventRepository eventRepository, AnomalyDetectionService anomalyDetectionService, LLMService llmService, NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.anomalyDetectionService = anomalyDetectionService;
        this.llmService = llmService;
        this.notificationService = notificationService;
    }

    @PostMapping("/detect")
    public String detectAnomaly(@RequestBody AnomalyDetectRequest request) throws JsonProcessingException {


        int[] isAnomalous = anomalyDetectionService.isAnomalous(request);
        if (isAnomalous[0]==1) {
            String prompt = "User normally creates 5-10 users per day. Today, they created " + isAnomalous[1] + ". Is this an anomaly?";
            String anomalyDetectionResponse = llmService.analyzeWithLLM(prompt);
            if(anomalyDetectionResponse.startsWith("Yes")) {
                notificationService.notifyCustomerIfAnomaly(anomalyDetectionResponse);
            }
            return anomalyDetectionResponse;
        }
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson object mapper

        Event event = new Event();
        event.setActionType(request.getActionType());
        event.setAccountId(request.getAccountId());
        event.setAnomaly(isAnomalous[0]!=0);
        event.setUserId(request.getUserId());
        try {
            // Convert JSON to String
            String detailsJson = objectMapper.writeValueAsString(request.getDetails());
            event.setDetails(detailsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Handle exception (log it)
        }

        anomalyDetectionService.save(event);
        return "✅ Normal Action";
    }

    @GetMapping("/test")
    public String test(){
        return "✅ Normal Action";
    }
}