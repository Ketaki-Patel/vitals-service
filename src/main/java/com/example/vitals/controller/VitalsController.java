package com.example.vitals.controller;
import com.example.vitals.dto.Reading;
import com.example.vitals.service.VitalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/readings")
@RequiredArgsConstructor
public class VitalsController {

    private final VitalsService vitalsService;

    @PostMapping
    public Mono<ResponseEntity<Void>> receiveReading(@RequestBody Reading reading) {
        return vitalsService.handleReading(reading)
                .thenReturn(ResponseEntity.accepted().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().<Void>build()));
    }

}
