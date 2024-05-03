package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kata.spring.boot_security.demo.util.PersonNotCreatedException;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundByIdException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PersonNotCreatedException.class)
    public ResponseEntity<Map<String, String>> handlePersonAlreadyExist(PersonNotCreatedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("username", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(PersonNotFoundByIdException.class)
    public ResponseEntity<Map<String, String>> handlePersonNotFoundById(PersonNotFoundByIdException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("id", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }


}
