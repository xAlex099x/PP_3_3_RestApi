package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.security.PersonDetails;
import ru.kata.spring.boot_security.demo.util.DTOConverter;

@Controller
public class PagesController {

    private final DTOConverter dtoConverter;
    public PagesController(DTOConverter dtoConverter) {
        this.dtoConverter = dtoConverter;
    }

    @GetMapping("/user")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
        return "/user";
    }

    @GetMapping("/admin_page")
    public String adminPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails details = (PersonDetails) auth.getPrincipal();
        model.addAttribute("person", dtoConverter.convertToDto(details.getPerson()));
        return "/admin_page";
    }
}
