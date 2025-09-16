package com.example.vitals.repository;
import com.example.vitals.entity.ReadingEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ReadingRepository extends R2dbcRepository<ReadingEntity, UUID> {

    @Modifying
    @Query("INSERT INTO readings (reading_id, patient_id, type, systolic, diastolic, hr, spo2, captured_at) VALUES (:readingId, :patientId, :type, :systolic, :diastolic, :hr, :spo2, :capturedAt)")
    Mono<Void> insert(ReadingEntity reading);

    // Find all readings for a specific patient
    Flux<ReadingEntity> findByPatientId(String patientId);

    // Find readings by type
    Flux<ReadingEntity> findByType(String type);

    // Find readings by patient and type
    Flux<ReadingEntity> findByPatientIdAndType(String patientId, String type);

    // Check if reading exists (for idempotency)
    Mono<Boolean> existsByReadingId(UUID readingId);


}
