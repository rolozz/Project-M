package com.java.project.userinfoservice.controllers;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRegisterAndUpdateController {

    private final AccountService accountService;

    @Autowired
    public AccountRegisterAndUpdateController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<AccountIdDto> findUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(accountService.findByName(username));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<AccountIdDto>> findSomeUsers(@RequestBody List<String> usernames) {
        return ResponseEntity.ok(accountService.findListByNames(usernames));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountIdDto accountIdDto) {
        return accountService.save(accountIdDto);
    }

    @PostMapping("/update/{username}")
    public ResponseEntity<String> update(@PathVariable("username") String username, @RequestBody AccountIdDto accountIdDto) {
        return accountService.update(username, accountIdDto);
    }
}
