package ru.loolzaaa.dockerjava.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.loolzaaa.dockerjava.model.Note;
import ru.loolzaaa.dockerjava.repositories.NoteRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/note")
public class NoteController {

    private final NoteRepository noteRepository;

    @GetMapping(path = "/all", produces = "application/json")
    Iterable<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    Note getNoteByUuid(@PathVariable("uuid") UUID uuid) {
        return noteRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("There is no note with uuid=" + uuid));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(produces = "application/json")
    Note createNote(@RequestBody String text) {
        Note newNote = new Note();
        newNote.setText(text);
        return noteRepository.save(newNote);
    }

    @PatchMapping(path = "/{uuid}", produces = "application/json")
    Note editNoteByUuid(@PathVariable("uuid") UUID uuid, @RequestBody String newText) {
        Note note = noteRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("There is no note with uuid=" + uuid));
        note.setText(newText);
        return noteRepository.save(note);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{uuid}")
    void deleteNoteByUuid(@PathVariable("uuid") UUID uuid) {
        Note note = noteRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("There is no note with uuid=" + uuid));
        noteRepository.delete(note);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    String handleNoSuchElementException(NoSuchElementException e) {
        return e.getMessage();
    }
}
