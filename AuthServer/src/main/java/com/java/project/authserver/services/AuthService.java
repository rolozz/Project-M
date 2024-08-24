package com.java.project.authserver.services;

import com.java.project.authserver.dto.RequestDto;
import com.java.project.authserver.dto.UpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService extends UserDetailsService {

    void register(RequestDto requestDto);

    ResponseEntity<?> authenticateUser(RequestDto requestDto);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void update(UpdateDto updateDto);

}
