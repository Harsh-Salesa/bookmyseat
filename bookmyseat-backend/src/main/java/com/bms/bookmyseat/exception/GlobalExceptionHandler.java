package com.bms.bookmyseat.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(Exception ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalid(Exception ex) {
        return ResponseEntity.status(401).body(ex.getMessage());

        }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<?> handleMovieExists(MovieAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(Map.of(
                "error", ex.getMessage()
        ));

    }
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<?> handleMovieNotFound(MovieNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", ex.getMessage()
        ));
    }
    @ExceptionHandler(TheaterNotFoundException.class)
    public ResponseEntity<?> handleTheaterNotFound(TheaterNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of(
                "error", ex.getMessage()
        ));
    }

    @ExceptionHandler(TheaterAlreadyExistsException.class)
    public ResponseEntity<?> handleTheaterExists(TheaterAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(Map.of(
                "error", ex.getMessage()
        ));

    }

}