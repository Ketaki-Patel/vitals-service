package com.example.vitals.dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
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
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    } else {
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("No error body")
                                .flatMap(errorBody -> {
                                    String errorMsg = String.format("Alert service error (%s): %s",
                                            response.statusCode(), errorBody);
                                    log.warn("{}", errorMsg);
                                    return Mono.error(new RuntimeException(errorMsg));
                                });
                    }
                });
    }


}
