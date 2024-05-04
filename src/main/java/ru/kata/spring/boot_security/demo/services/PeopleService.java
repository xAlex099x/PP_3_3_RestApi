package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.models.Person;
import java.util.List;

public interface PeopleService {
    public List<Person> allPeople();

    public Person userByUsername(String username);

    public Person userById(long id);

    public void addUser(PersonDTO personDto);

    public void updateUser(long id, PersonDTO updatedUserDto);

    public void deleteUser(long id);
}
