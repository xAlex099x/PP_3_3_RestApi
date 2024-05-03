package ru.kata.spring.boot_security.demo.util;

public class PersonNotFoundByIdException extends RuntimeException  {

    public PersonNotFoundByIdException(String message){
        super(message);
    }

}
