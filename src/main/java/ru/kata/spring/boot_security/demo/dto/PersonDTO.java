package ru.kata.spring.boot_security.demo.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class PersonDTO {

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

    private List<String> roles;

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
