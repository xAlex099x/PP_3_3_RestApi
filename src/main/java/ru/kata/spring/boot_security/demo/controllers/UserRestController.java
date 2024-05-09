package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.security.PersonDetails;
import ru.kata.spring.boot_security.demo.util.DTOConverter;

@RestController
public class UserRestController {

    private final DTOConverter dtoConverter;

    public UserRestController(DTOConverter dtoConverter) {
        this.dtoConverter = dtoConverter;
    }

    //    @GetMapping("/user_page")
//    public String userPage(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        PersonDetails details = (PersonDetails) auth.getPrincipal();
//        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
//        return "/user_page";
//    }
    @GetMapping("/user_page")
    public ResponseEntity<?> userPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        PersonDTO personDto = dtoConverter.convertToDto(details.getPerson());

        return ResponseEntity.ok(personDto); // Возвращает объект personDto в формате JSON с HTTP статусом 200
    }

    //    @GetMapping("/admin_page")
//    public String adminPage(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        PersonDetails details = (PersonDetails) auth.getPrincipal();
//        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
//        return "/admin_page";
//    }
    @GetMapping("/user_info")
    public ResponseEntity<?> userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        PersonDTO personDto = dtoConverter.convertToDto(details.getPerson());

        return ResponseEntity.ok(personDto);
    }
}
