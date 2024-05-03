package ru.kata.spring.boot_security.demo.util;

public class PersonNotCreatedException extends RuntimeException{
    public PersonNotCreatedException(String message) {
        super(message);
    }
}
