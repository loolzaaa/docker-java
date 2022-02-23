package ru.loolzaaa.dockerjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import ru.loolzaaa.dockerjava.model.Note;

import java.util.UUID;

@Configuration
public class JdbcConfiguration {
    @Bean
    BeforeSaveCallback<Note> beforeSaveCallback() {
        return (note, mutableAggregateChange) -> {
            if (note.getId() == null) {
                note.setId(UUID.randomUUID());
            }
            return note;
        };
    }
}
