package com.java.project.authserver.controllers;

import com.java.project.authserver.dto.RequestDto;
import com.java.project.authserver.dto.UpdateDto;
import com.java.project.authserver.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/security-test")
    public String test() {
        return "privet, dubolom!!!";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestDto requestDto) {
        authService.register(requestDto);
        return ResponseEntity.ok("You Are Welcome!!!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestDto requestDto) {
        return authService.authenticateUser(requestDto);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody UpdateDto updateDto) {
        authService.update(updateDto);
        return ResponseEntity.ok("Updated");
    }
}
