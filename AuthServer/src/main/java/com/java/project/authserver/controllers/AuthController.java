package com.java.project.authserver.controllers;

import com.java.project.authserver.dto.LoginRequest;
import com.java.project.authserver.entities.Person;
import com.java.project.authserver.services.AuthService;
import com.java.project.authserver.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authenticationService;
    private final PersonService personService;

    public AuthController(AuthService authenticationService, PersonService personService) {
        this.authenticationService = authenticationService;
        this.personService = personService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authenticationService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Person person) {
        try {
            personService.register(person);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }
}
