package com.example.vitals.repository;

import com.example.vitals.entity.ReadingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReadingEntityInsertHelper {

    private final R2dbcEntityTemplate template;

    @Autowired
    public ReadingEntityInsertHelper(R2dbcEntityTemplate template) {
        this.template = template;
    }

    public Mono<Void> insertReading(ReadingEntity entity) {
        return template.insert(ReadingEntity.class)
                .using(entity)
                .then();
    }
}

