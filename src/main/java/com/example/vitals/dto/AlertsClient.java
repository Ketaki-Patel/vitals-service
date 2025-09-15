package com.example.vitals.dto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AlertsClient {

    private final WebClient webClient;

    public AlertsClient(@Value("${alerts-service.base-url:http://localhost:8082}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Void> sendToAlerts(Reading reading) {
        return webClient.post()
                .uri("/evaluate")
                .bodyValue(reading)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(ex -> {
                    // Log error or handle retry logic
                    ex.printStackTrace();
                    return Mono.empty();
                });
    }
}
