package com.hackathon.anomaly.detector.service;

import com.hackathon.anomaly.detector.model.AnomalyDetectRequest;
import com.hackathon.anomaly.detector.model.Event;
import com.hackathon.anomaly.detector.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AnomalyDetectionService {
    private final EventRepository eventRepository;

    public AnomalyDetectionService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public int[] isAnomalous(AnomalyDetectRequest newEvent) {

        // Get the timestamp range for the last 5 full days (excluding today)
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime fiveDaysAgo = startOfToday.minusDays(5);

        // Fetch events from the last 5 full days (excluding today) for the given user
        List<Event> pastEvents = eventRepository.findByAccountIdAndCreatedAtBetween(newEvent.getAccountId(), fiveDaysAgo, startOfToday.minusSeconds(1));

        // Group events by day and count occurrences per day
        Map<LocalDate, Long> dailyCounts = pastEvents.stream()
                .collect(Collectors.groupingBy(e -> e.getCreatedAt().toLocalDate(), Collectors.counting()));

        // Convert daily counts to a list
        List<Long> counts = dailyCounts.values().stream().collect(Collectors.toList());
//Needs at least 3 days of data.
        if (counts.size() < 3) return new int[]{0,1}; // Not enough data to determine anomaly

        // Calculate mean
        double mean = counts.stream().mapToLong(Long::longValue).average().orElse(0.0);

        // Calculate standard deviation
        double stdDev = Math.sqrt(counts.stream()
                .mapToDouble(count -> Math.pow(count - mean, 2))
                .average()
                .orElse(0.0));

        // Compute upper threshold
        double upperThreshold = mean + 3 * stdDev;

        // Get today's event count (including the new event)
        long todayCount = dailyCounts.getOrDefault(LocalDate.now(), 0L) + 1;

        if(todayCount > upperThreshold){
            // Flag anomaly if today's count exceeds the threshold
            return new int[]{1,(int)todayCount};
        }
        return new int[]{0,(int)todayCount};
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
