package com.example.vitals.service;

import com.example.vitals.dto.AlertsClient;
import com.example.vitals.dto.Reading;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class VitalsService {

    private final ConcurrentHashMap<UUID, Reading> readingStore = new ConcurrentHashMap<>();
    private final AlertsClient alertsClient;

    public Mono<Void> handleReading(Reading reading) {
        log.info("reading before validation:  " + reading);
        if (reading.getReadingId() == null || reading.getPatientId() == null || reading.getType() == null) {
            return Mono.error(new IllegalArgumentException("Missing required fields"));
        }

        if (!validateFieldsByType(reading)) {
            return Mono.error(new IllegalArgumentException("Missing type-specific fields"));
        }

        // Idempotency check
        if (readingStore.putIfAbsent(reading.getReadingId(), reading) != null) {
            return Mono.empty(); // Already exists
        }

        log.info("reading:  " + reading);

//       // return Mono.empty();
//
//        // Async forward to alerts service
       return alertsClient.sendToAlerts(reading).then();

    }

    private boolean validateFieldsByType(Reading reading) {
        return switch (reading.getType()) {
            case "BP" -> reading.getSystolic() != null && reading.getDiastolic() != null;
            case "HR" -> reading.getHr() != null;
            case "SPO2" -> reading.getSpo2() != null;
            default -> false;
        };
    }


}
