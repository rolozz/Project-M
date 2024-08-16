package com.java.project.userinfoservice.controllers;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRegisterAndUpdateController {

    private final AccountService accountService;

    @Autowired
    public AccountRegisterAndUpdateController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountIdDto accountIdDto) throws IllegalAccessException {
        accountService.save(accountIdDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id,@RequestBody AccountIdDto accountIdDto){
        accountService.update(id,accountIdDto);
        return ResponseEntity.ok("Ok");
    }
}
