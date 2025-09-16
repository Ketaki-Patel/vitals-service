package com.example.vitals.mapper;
import com.example.vitals.dto.Reading;
import com.example.vitals.entity.ReadingEntity;
import org.springframework.stereotype.Component;

/**
 * we can us mapstruct but keeping it simple for this project
 */
@Component
public class ReadingMapper {

    public ReadingEntity toEntity(Reading reading) {
        return ReadingEntity.builder()
                .readingId(reading.getReadingId())
                .patientId(reading.getPatientId())
                .type(reading.getType())
                .systolic(reading.getSystolic())
                .diastolic(reading.getDiastolic())
                .hr(reading.getHr())
                .spo2(reading.getSpo2())
                .capturedAt(reading.getCapturedAt())
                .build();
    }

    public Reading toDto(ReadingEntity entity) {
        return Reading.builder()
                .readingId(entity.getReadingId())
                .patientId(entity.getPatientId())
                .type(entity.getType())
                .systolic(entity.getSystolic())
                .diastolic(entity.getDiastolic())
                .hr(entity.getHr())
                .spo2(entity.getSpo2())
                .capturedAt(entity.getCapturedAt())
                .build();
    }
}
