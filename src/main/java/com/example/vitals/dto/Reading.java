package com.example.vitals.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Schema(description = "Patient vital reading")
@Schema(
        description = """
        Patient vital reading.

        Required fields based on `type`:
        - If type = **BP**, then `systolic` and `diastolic` are required.
        - If type = **HR**, then `hr` is required.
        - If type = **SPO2**, then `spo2` is required.
        """
)

public class Reading {

    @NotNull(message = "Reading ID must not be null")
    @Schema(description = "Unique identifier for the reading", example = "11111111-1111-1111-1111-111111111111")
    private UUID readingId;

    @NotBlank(message = "patient ID is required")
    @Schema(description = "ID of the patient", example = "p-001")
    private String patientId;

    @NotBlank(message = "type is required")
    @Schema(description = "Type of reading (BP, HR, SPO2)", example = "BP", allowableValues = {"BP", "HR", "SPO2"})
    private String type;

    @Schema(description = "Systolic blood pressure (upper number), required if type = BP", example = "150", nullable = true)
    private Integer systolic;

    @Schema(description = "Diastolic blood pressure (lower number), required if type = BP", example = "95", nullable = true)
    private Integer diastolic;

    @Schema(description = "Heart rate (beats per minute), required if type = HR",  nullable = true)
    private Integer hr;

    @Schema(description = "Oxygen saturation percentage, required if type = SPO2",  nullable = true)
    private Integer spo2;

    @Schema(description = "ISO 8601 timestamp when the reading was captured", example = "2025-08-01T12:00:00Z")
    private Instant capturedAt;

    // getters and setters
}


