package ru.loolzaaa.dockerjava.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.loolzaaa.dockerjava.model.Note;

import java.util.UUID;

public interface NoteRepository extends CrudRepository<Note, UUID> {
}
