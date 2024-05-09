package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.services.PeopleServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest_api")
public class AdminRestController {

    private final PeopleServiceImpl peopleServiceImpl;

    @Autowired
    public AdminRestController(PeopleServiceImpl peopleServiceImpl) {
        this.peopleServiceImpl = peopleServiceImpl;
    }

    @GetMapping("/get_people")
    public ResponseEntity<List<PersonDTO>> getPeople() {
        return ResponseEntity.ok(peopleServiceImpl
                .allPeople());
    }

    @PostMapping("/new")
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid PersonDTO personDTO) {
        peopleServiceImpl.addUser(personDTO);

        return ResponseEntity.ok(personDTO);
    }

    @PostMapping("/edit")
    public ResponseEntity<PersonDTO> edit(@RequestBody @Valid PersonDTO personDTO) {
        peopleServiceImpl.updateUser(personDTO.getId(), personDTO);

        return ResponseEntity.ok(personDTO);
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody PersonDTO personDTO) {
        peopleServiceImpl.deleteUser(personDTO.getId());

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
