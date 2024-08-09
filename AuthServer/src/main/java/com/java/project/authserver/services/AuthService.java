package com.java.project.authserver.services;

import org.springframework.security.core.Authentication;

public interface AuthService {

    String authenticate(String login, String password);

    String generateToken(Authentication authentication);

}
