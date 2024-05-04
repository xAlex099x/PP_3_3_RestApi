package ru.kata.spring.boot_security.demo.dto;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kata.spring.boot_security.demo.configs.RolesEnum;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.RoleService;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class PersonDTO {

    @Autowired
    private static RoleService roleServiceImpl;
    private static final ModelMapper modelMapper = new ModelMapper();
    private long id;
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 60, message = "Имя пользователя должно быть от 2 до 60 символов")
    private String username;

    @NotEmpty
    @Size(min = 2, max = 16, message = "Пароль должен быть от 2 до 16 символов")
    private String password;

    @NotEmpty(message = "Email не должен быть пустым")
    @Email
    private String email;

    private Set<String> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
