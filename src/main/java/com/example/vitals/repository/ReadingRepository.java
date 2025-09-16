package com.example.vitals.repository;
import com.example.vitals.entity.ReadingEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ReadingRepository extends R2dbcRepository<ReadingEntity, UUID> {

    Flux<ReadingEntity> findByPatientId(String patientId);

    Flux<ReadingEntity> findByType(String type);

    Flux<ReadingEntity> findByPatientIdAndType(String patientId, String type);

    // Check if reading exists (for idempotency)
    Mono<Boolean> existsByReadingId(UUID readingId);

}
