package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.PeopleServiceImpl;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.util.DTOConverter;
import java.util.HashSet;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleServiceImpl roleServiceImpl;
    private final PeopleServiceImpl peopleServiceImpl;

    private final DTOConverter dtoConverter;


    @Autowired
    public StartupApplicationListener(RoleServiceImpl roleServiceImpl, PeopleServiceImpl peopleServiceImpl, DTOConverter dtoConverter) {
        this.roleServiceImpl = roleServiceImpl;
        this.peopleServiceImpl = peopleServiceImpl;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Проверяем наличие ролей Admin и User в базе данных, а так же root пользователя.
        Role adminRole = roleServiceImpl.findByName(RolesEnum.ADMIN.getRoleName());
        Role userRole = roleServiceImpl.findByName(RolesEnum.USER.getRoleName());
        Person Admin = peopleServiceImpl.userByUsername("Testuser");

        if (adminRole == null) {
            roleServiceImpl.save(new Role(RolesEnum.ADMIN.getRoleName()));
        }
        if (userRole == null) {
            roleServiceImpl.save(new Role("ROLE_USER"));
        }
        if (Admin == null) {
            Person person = new Person(
                    "Testuser",
                    "Testuser",
                    "Test@Test.com",
                    new HashSet<Role>());
            person.getRoles().add(roleServiceImpl.findByName(RolesEnum.ADMIN.getRoleName()));
            peopleServiceImpl.addUser(dtoConverter.convertToDto(person));
        }
    }
}
