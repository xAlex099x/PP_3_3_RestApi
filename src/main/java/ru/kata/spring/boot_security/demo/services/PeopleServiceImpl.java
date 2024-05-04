package ru.kata.spring.boot_security.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.repositories.PeopleRepository;
import ru.kata.spring.boot_security.demo.util.DTOConverter;
import ru.kata.spring.boot_security.demo.util.PersonNotCreatedException;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundByIdException;
import ru.kata.spring.boot_security.demo.util.PersonNotFoundByUsernameException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;

    private final DTOConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleServiceImpl(PeopleRepository peopleRepository,
                             RoleServiceImpl roleServiceImpl, PasswordEncoder passwordEncoder, ModelMapper modelMapper, DTOConverter dtoConverter) {
        this.peopleRepository = peopleRepository;
        this.dtoConverter = dtoConverter;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> allPeople() {
        return peopleRepository.findAll();
    }

    public Person userByUsername(String username) {
        Optional<Person> result = peopleRepository.findByUsername(username);
        return result.orElseThrow(() -> new PersonNotFoundByUsernameException("User not found"));
    }

    public Person userById(long id) {
        Optional<Person> result = peopleRepository.findById(id);
        return result.orElseThrow(() -> new PersonNotFoundByIdException("User not found"));
    }

    @Transactional
    public void addUser(PersonDTO personDto) {
        Person person = dtoConverter.convertToPerson(personDto);
        try {
            this.userByUsername(person.getUsername());
            throw new PersonNotCreatedException("Пользователь с таким username уже существует");
        } catch (PersonNotFoundByUsernameException e) {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            peopleRepository.save(person);
        }
    }

    @Transactional
    public void updateUser(long id, PersonDTO updatedUserDTO) {
        Person updatedUser = dtoConverter.convertToPerson(updatedUserDTO);

        this.userById(id);
        updatedUser.setId(id);
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        peopleRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(long id) {
        this.userById(id);
        peopleRepository.deleteById(id);
    }

}
