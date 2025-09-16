package com.example.vitals.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("readings")
public class ReadingEntity {

    @Id
    private UUID readingId;
    private String patientId;
    private String type;
    private Integer systolic;
    private Integer diastolic;
    private Integer hr;
    private Integer spo2;
    private Instant capturedAt;
}
