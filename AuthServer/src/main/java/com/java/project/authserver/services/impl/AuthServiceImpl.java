package com.java.project.authserver.services.impl;

import com.java.project.authserver.services.AuthService;
import com.java.project.authserver.services.PersonService;
import com.java.project.authserver.services.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final PersonService personService;


    private String jwtSecret = "3sR0w8QwX7I5O4U6K1L4V3N2M8T2Y9Q1R2F5G7H1K3J5L0P9S2W7Q8N5R4M3J2";
    private long jwtExpiration = 3600000;

    public AuthServiceImpl(AuthenticationManager authenticationManager, RedisService redisService, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
        this.personService = personService;
    }

    public String authenticate(String login, String password) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            return generateToken(authentication);
        } catch (AuthenticationException e) {
            // Логируем исключение
            log.error("Authentication failed: ", e);
            throw e;
        }
    }

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", authentication.getName());
        claims.put("roles", authentication.getAuthorities());

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();

        redisService.storeToken(authentication.getName(), jwtToken);

        return jwtToken;
    }
}
