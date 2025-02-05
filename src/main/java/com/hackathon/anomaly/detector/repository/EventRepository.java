package com.hackathon.anomaly.detector.repository;

import com.hackathon.anomaly.detector.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {



    List<Event> findByAccountIdAndCreatedAtBetween(String accountId, LocalDateTime fiveDaysAgo, LocalDateTime localDateTime);
}
