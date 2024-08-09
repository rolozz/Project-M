package com.java.project.authserver.services;

import com.java.project.authserver.entities.Person;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface PersonService extends UserDetailsService {

    UserDetails loadUserByUsername(String username)throws UsernameNotFoundException;
    Person register(Person person);
    Optional<Person> findByLogin(String login);

}
