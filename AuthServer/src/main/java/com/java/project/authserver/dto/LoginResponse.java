package com.java.project.authserver.dto;

import com.java.project.authserver.entities.Person;
import com.java.project.authserver.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginResponse {

    Person person;
    String token;

}
