package com.java.project.authserver.services;

import com.java.project.authserver.dto.RequestDto;
import com.java.project.authserver.entities.Person;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {

    void register(RequestDto requestDto);

    Person authenticateUser(RequestDto requestDto);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
