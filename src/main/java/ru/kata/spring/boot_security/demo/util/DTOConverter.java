package ru.kata.spring.boot_security.demo.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.configs.RolesEnum;
import ru.kata.spring.boot_security.demo.dto.PersonDTO;
import ru.kata.spring.boot_security.demo.models.Person;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.services.RoleService;
import java.util.HashSet;
import java.util.Set;
@Component
public class DTOConverter {

    private static RoleService roleServiceImpl;
    private static ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public DTOConverter(RoleService roleServiceImpl) {
        this.roleServiceImpl = roleServiceImpl;
    }

    public PersonDTO convertToDto(Person person) {
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);

        Set<String> roles = new HashSet<>();
        for (Role role : person.getRoles()) {
            RolesEnum.getRoleNameWithoutPrefix(role.getName()).ifPresent(roles::add);
        }
        personDTO.setRoles(roles);

        return personDTO;
    }

    public Person convertToPerson(PersonDTO personDTO) {
        if (personDTO.getRoles() == null) {
            Set<String> roles = new HashSet<>();
            roles.add(RolesEnum.USER.getRoleName());
            personDTO.setRoles(roles);
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
