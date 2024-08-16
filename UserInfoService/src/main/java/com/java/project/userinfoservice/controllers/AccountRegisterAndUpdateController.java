package com.java.project.userinfoservice.controllers;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import com.java.project.userinfoservice.entities.AccountId;
import com.java.project.userinfoservice.repositories.AccountIdRepository;
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
    private final AccountIdRepository accountIdRepository;

    @Autowired
    public AccountRegisterAndUpdateController(AccountService accountService, AccountIdRepository accountIdRepository) {
        this.accountService = accountService;
        this.accountIdRepository = accountIdRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountIdDto accountIdDto) throws IllegalAccessException {
        accountService.save(accountIdDto);
        RequestDto requestDto = new RequestDto(accountIdDto.getUsername(), accountIdDto.getPassword());
        accountService.saveBackUp(requestDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody AccountIdDto accountIdDto) {
        AccountId accountId = accountIdRepository.findById(id).orElseThrow(()-> new RuntimeException("empty"));
        UpdateDto updateDto = new UpdateDto();
        updateDto.setPreUpdate(accountId.getUsername());
        updateDto.setUpdate(accountIdDto.getUsername());
        updateDto.setPassword(accountIdDto.getPassword());
        accountService.updateBackUp(updateDto);
        accountService.update(id, accountIdDto);
        return ResponseEntity.ok("Ok");
    }
}
