package com.example.vitals.service;

import com.example.vitals.dto.AlertsClient;
import com.example.vitals.dto.Reading;
import com.example.vitals.entity.ReadingEntity;
import com.example.vitals.exception.ReadingNotFoundException;
import com.example.vitals.mapper.ReadingMapper;
import com.example.vitals.repository.ReadingEntityInsertHelper;
import com.example.vitals.repository.ReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class VitalsService {

    private final ConcurrentHashMap<UUID, Reading> readingStore = new ConcurrentHashMap<>();
    private final AlertsClient alertsClient;
    private final ReadingRepository readingRepository;
    private final ReadingMapper readingMapper;
    private final ReadingEntityInsertHelper readingEntityInsertHelper;

    /**
     *  take home project description talks about only using concurrentHash, but I also
     *  persist data in inMemory h2 database
     *  with some small config change we can persist h2 data in file system. I am not persisting in file
     *  to keep it simple just showcasing two types of in memory persistence here.
     * @param reading
     * @return
     */
    public Mono<Void> handleReading(Reading reading) {
        log.info("Reading before validation: {}", reading);

        if (!validateFieldsByType(reading)) {
            log.info("Missing or mismatched type, Allowable types are only [BP, HR, SPO2]");
            throw new IllegalArgumentException("Missing or mismatched type, Allowable types are only [BP, HR, SPO2]");
        }

        // In-memory idempotency check
        if (readingStore.putIfAbsent(reading.getReadingId(), reading) != null) {
            log.info("Duplicate reading detected. Already handled for readingID {}", reading.getReadingId());
            return Mono.empty(); // Already handled
        }

        log.info("Reading passed in-memory idempotency check: {}", reading);

        return checkAndInsertReadingInDB(reading)
                .then(sendReadingToAlerts(reading));

    }


    private Mono<Void> checkAndInsertReadingInDB(Reading reading) {
        return readingRepository.existsByReadingId(reading.getReadingId())
                .flatMap(exists -> {

                    //DB-level idempotency check (redundant in our case as we already checked readingStore(concurrentHash)
                    if (exists) {
                        log.info("Reading with ID {} already exists in DB, skipping insert", reading.getReadingId());
                        return Mono.empty();
                    }

                    ReadingEntity entity = readingMapper.toEntity(reading);
                    log.info("Mapped ReadingEntity: {}", entity);

                    // Use R2dbcEntityTemplate to insert
                    // readingRepository.save(entity) was not behaving as expected so I made choice to use R2dbcEntityTemplate
                    return readingEntityInsertHelper.insertReading(entity)
                            .doOnSuccess(v -> log.info("✅ Successfully inserted reading into DB"))
                            .onErrorMap(ex -> {
                                log.error("❌ DB insert failed: {}", ex.getMessage(), ex);
                                return new RuntimeException("DB failed", ex);
                            });
                });
    }

    /**
     * if alert fails ,code will just log and resume i.e original api call will not throw error
     * @param reading
     * @return
     */
    private Mono<Void> sendReadingToAlerts(Reading reading) {
        return alertsClient.sendToAlerts(reading)
                .doOnSuccess(v -> log.info("📨 Sent reading to alerts service"))
                .onErrorResume(ex -> {
                    // Log error or handle retry logic
                    log.warn("Failed to send alert: {}", ex.toString());
                    return Mono.empty();
    });

    }

    private boolean validateFieldsByType(Reading reading) {
        return switch (reading.getType()) {
            case "BP" -> reading.getSystolic() != null && reading.getDiastolic() != null;
            case "HR" -> reading.getHr() != null;
            case "SPO2" -> reading.getSpo2() != null;
            default -> false;
        };
    }


    // Additional methods for querying data
    //following methods are extra methods which retrieves data from the h2 database.
    public Flux<Reading> getReadingsByPatientId(String patientId) {
        return readingRepository.findByPatientId(patientId)
                .map(readingMapper::toDto);
    }

    public Flux<Reading> getReadingsByType(String type) {
        return readingRepository.findByType(type)
                .map(readingMapper::toDto);
    }

    public Mono<Reading> getReadingById(UUID readingId) {
        return readingRepository.findById(readingId)
                .map(readingMapper::toDto)
                .switchIfEmpty(Mono.error(new ReadingNotFoundException("Reading not found with ID: " + readingId)));
    }

    public Flux<Reading> getAllReadings() {
        return readingRepository.findAll()
                .map(readingMapper::toDto);
    }

}
