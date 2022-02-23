package ru.loolzaaa.dockerjava.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@Table("notes")
public class Note {
    @Id
    private UUID id;
    private String text;
}
