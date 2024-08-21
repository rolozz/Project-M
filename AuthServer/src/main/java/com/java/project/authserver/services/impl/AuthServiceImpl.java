package com.java.project.authserver.services.impl;

import com.java.project.authserver.dto.RequestDto;
import com.java.project.authserver.dto.UpdateDto;
import com.java.project.authserver.entities.Person;
import com.java.project.authserver.entities.Role;
import com.java.project.authserver.jwt.JwtUtil;
import com.java.project.authserver.repositories.PersonRepository;
import com.java.project.authserver.repositories.RoleRepository;
import com.java.project.authserver.services.AuthService;
import com.java.project.authserver.services.RedisService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(
            PersonRepository personRepository,
            @Lazy PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            @Lazy AuthenticationManager authenticationManager,
            RedisService redisService,
            JwtUtil jwtUtil
    ) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void register(RequestDto requestDto) {
        Optional<Person> preReg = personRepository.findByUsername(requestDto.getUsername());
        if (preReg.isPresent()) {
            Person updatedPerson = preReg.get();
            updatedPerson.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            personRepository.save(updatedPerson);
        } else {
            final Person newPerson = new Person();
            newPerson.setUsername(requestDto.getUsername());
            newPerson.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            newPerson.setRole(roleRepository.findRoleByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("role not found")));
            personRepository.save(newPerson);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> authenticateUser(RequestDto requestDto) {
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword())
                    );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final Person authPerson = personRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("username not found" + requestDto.getUsername()));
        if (!passwordEncoder.matches(requestDto.getPassword(), authPerson.getPassword())) {
            throw new BadCredentialsException("invalid password");
        } else {
            String token = jwtUtil.generateToken(authPerson);
            redisService.saveValue(requestDto.getUsername(), token);
            return ResponseEntity.ok(token);
        }
    }

    @Override
    @Transactional
    public void update(UpdateDto updateDto) {
        Person updatedPerson = personRepository.findByUsername
                (updateDto.getPreUpdate()).orElseThrow(() -> new RuntimeException("empty"));
        updatedPerson.setUsername(updateDto.getUpdate());
        updatedPerson.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        personRepository.save(updatedPerson);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found" + username));
        Role role = person.getRole();
        GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        return new org.springframework.security.core.userdetails.User(
                person.getUsername(),
                person.getPassword(),
                Collections.singletonList(authority)
        );
    }
}

