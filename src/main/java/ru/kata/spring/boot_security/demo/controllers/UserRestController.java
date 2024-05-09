package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.security.PersonDetails;
import ru.kata.spring.boot_security.demo.util.DTOConverter;

@Controller
public class UserRestController {

    private final DTOConverter dtoConverter;
    public UserRestController(DTOConverter dtoConverter) {
        this.dtoConverter = dtoConverter;
    }

    @GetMapping("/user_page")
    public String userPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
        return "/user_page";
    }

    @GetMapping("/admin_page")
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
        return "/admin_page";
    }
}
