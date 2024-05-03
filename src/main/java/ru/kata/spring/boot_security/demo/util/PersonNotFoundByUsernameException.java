package ru.kata.spring.boot_security.demo.util;

public class PersonNotFoundByUsernameException extends RuntimeException {

    public PersonNotFoundByUsernameException(String message) {
        super(message);
    }
}
