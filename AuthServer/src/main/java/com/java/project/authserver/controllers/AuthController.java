package com.java.project.authserver.controllers;

import com.java.project.authserver.dto.RequestDto;
import com.java.project.authserver.jwt.JwtUtil;
import com.java.project.authserver.services.AuthService;
import com.java.project.authserver.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, AuthenticationManager authenticationManager, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/redis-test")
    public String testRedis(){
        redisService.saveValue("testKey", "пусть это будет тут");
        String string = redisService.getValue("testKey");
        return "рэдис: " + string;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/security-test")
    public String test() {
        return "privet, dubolom!!!";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestDto requestDto) {
        System.out.println("Register endpoint called");
        authService.register(requestDto);
        return ResponseEntity.ok("You Are Welcome!!!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestDto requestDto) {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword())
                    );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(jwtUtil.generateToken(authService.authenticateUser(requestDto)));
    }
}
