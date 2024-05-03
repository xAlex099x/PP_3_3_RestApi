package ru.kata.spring.boot_security.demo.util;

public class PersonNotEditException extends RuntimeException {

    public PersonNotEditException(String message) {
        super(message);
    }
}
