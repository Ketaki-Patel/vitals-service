package com.example.vitals.controller;
import com.example.vitals.dto.Reading;
import com.example.vitals.service.VitalsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/readings")
@RequiredArgsConstructor
@Slf4j
public class VitalsController {

    private final VitalsService vitalsService;

    @PostMapping
    public Mono<ResponseEntity<Void>> receiveReading(@Valid @RequestBody Reading reading) {
        log.info("reading received: "+ reading);
        return vitalsService.handleReading(reading)
                .thenReturn(ResponseEntity.accepted().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().<Void>build()));
    }

    // Get all readings
    @GetMapping
    public Flux<Reading> getAllReadings() {
        return vitalsService.getAllReadings();
    }

    // Get reading by ID
    @GetMapping("/{readingId}")
    public Mono<ResponseEntity<Reading>> getReadingById(@PathVariable @NonNull UUID readingId) {
        return vitalsService.getReadingById(readingId)
                .map(ResponseEntity::ok);
    }

    // Get readings by patient ID
    @GetMapping("/patient/{patientId}")
    public Flux<Reading> getReadingsByPatientId(@PathVariable  String patientId) {
        return vitalsService.getReadingsByPatientId(patientId);
    }

    // Get readings by type
    @GetMapping("/type/{type}")
    public Flux<Reading> getReadingsByType(@PathVariable @NotBlank String type) {
        return vitalsService.getReadingsByType(type);
    }

}
