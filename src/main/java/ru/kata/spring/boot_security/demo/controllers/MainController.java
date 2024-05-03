package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.PeopleServiceImpl;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest_api")
public class MainController {

    private final RoleServiceImpl roleServiceImpl;
    private final PeopleServiceImpl peopleServiceImpl;
    private final ModelMapper modelMapper;

    @Autowired
    public MainController(PeopleServiceImpl peopleServiceImpl,
                          RoleServiceImpl roleServiceImpl, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.peopleServiceImpl = peopleServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get_people")
    public ResponseEntity<List<PersonDTO>> getPeople() {
        return ResponseEntity.ok(peopleServiceImpl
                .allPeople()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/new")
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonDTO personDTO) {
        System.out.println("before create");
        Person person = convertToPerson(personDTO);
        peopleServiceImpl.addUser(person);
        return ResponseEntity.ok(convertToDto(person));
    }

    @PostMapping("/edit")
    public ResponseEntity<PersonDTO> edit(@RequestBody @Valid PersonDTO personDTO) {
        Person person = convertToPerson(personDTO);
        peopleServiceImpl.updateUser(person.getId(), person);

        return ResponseEntity.ok(convertToDto(person));
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody PersonDTO personDTO) {
        peopleServiceImpl.deleteUser(personDTO.getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private PersonDTO convertToDto(Person person) {
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);

        List<String> roles = new ArrayList<>();
        for (Role role : person.getRoles()) {
            roles.add(role.getName());
        }
        personDTO.setRoles(roles);

        return personDTO;
    }

    private Person convertToPerson(PersonDTO personDTO) {

        if (personDTO.getRoles() == null) {
            List<String> tempRole = new ArrayList<>();
            tempRole.add("ROLE_USER");
            personDTO.setRoles(tempRole);
        }
        Person person = modelMapper.map(personDTO, Person.class);

        Set<Role> roles = new HashSet<>();
        for (String role : personDTO.getRoles()) {
            roles.add(roleServiceImpl.findByName(role));
        }
        person.setRoles(roles);

        return person;
    }
}
