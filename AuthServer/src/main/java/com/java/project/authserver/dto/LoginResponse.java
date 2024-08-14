package com.java.project.authserver.dto;

import com.java.project.authserver.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginResponse {

    Person person;
    String token;

}
