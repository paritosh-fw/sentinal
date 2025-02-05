package com.hackathon.anomaly.detector.service;

import com.hackathon.anomaly.detector.model.AnomalyDetectionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LLMService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String LLM_API_URL = "http://localhost:6000/api/llm"; // Replace with actual LLM

    public String analyzeWithLLM(String prompt) {
        Map<String, String> request = new HashMap<>();
        request.put("prompt", prompt);

        AnomalyDetectionResponse response = restTemplate.postForObject(LLM_API_URL, request, AnomalyDetectionResponse.class);
        return response !=null ? response.getResponse().trim() : null;
    }
}
