package com.java.project.authserver.services.impl;

import com.java.project.authserver.entities.Person;
import com.java.project.authserver.entities.Role;
import com.java.project.authserver.repositories.PersonRepository;
import com.java.project.authserver.repositories.RoleRepository;
import com.java.project.authserver.services.PersonService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(person.getLogin())
                .password(person.getPassword())
                .authorities(person.getRole().getName())
                .build();
    }

    @Override
    public Person register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        if (person.getRole() == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            person.setRole(userRole);
        }
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> findByLogin(String login) {
        return personRepository.findByLogin(login);
    }

}
