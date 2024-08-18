package com.java.project.userinfoservice.controllers;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import com.java.project.userinfoservice.mapper.AccountIdMapper;
import com.java.project.userinfoservice.repositories.AccountIdRepository;
import com.java.project.userinfoservice.services.AccountService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Supplier;

@RestController
public class AccountRegisterAndUpdateController {

    private final AccountService accountService;
    private final CircuitBreaker userInfoCircuitBreaker;


    @Autowired
    public AccountRegisterAndUpdateController(AccountService accountService, AccountIdRepository accountIdRepository, CircuitBreaker userInfoCircuitBreaker, AccountIdMapper mapper) {
        this.accountService = accountService;
        this.userInfoCircuitBreaker = userInfoCircuitBreaker;
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
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
                    accountService.saveBackUp(new RequestDto(accountIdDto.getUsername(), accountIdDto.getPassword()));
                    return "successful";
                }).withCircuitBreaker(userInfoCircuitBreaker)
                .withFallback(throwable -> ("backup failed - using fallback"))
                .decorate();
        String fallBackResult = decoratedSupplier.get();
        if (fallBackResult.startsWith("backup failed")) {
            return ResponseEntity.status(503).body(fallBackResult);
        }
        accountService.save(accountIdDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/update/{username}")
    public ResponseEntity<String> update(@PathVariable("username") String username, @RequestBody AccountIdDto accountIdDto) {
        AccountIdDto preAccountIdDto = accountService.findByName(username);
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
                    UpdateDto updateDto = new UpdateDto();
                    updateDto.setPreUpdate(preAccountIdDto.getUsername());
                    updateDto.setUpdate(accountIdDto.getUsername());
                    updateDto.setPassword(accountIdDto.getPassword());
                    accountService.updateBackUp(updateDto);
                    return "Update backUp successful";
                }).withCircuitBreaker(userInfoCircuitBreaker)
                .withFallback(throwable -> "Update backUp failed - using fallback")
                .decorate();
        String fallbackResult = decoratedSupplier.get();
        if (fallbackResult.startsWith("Update backUp failed")) {
            return ResponseEntity.status(503).body(fallbackResult);
        }
        accountService.update(username, accountIdDto);
        return ResponseEntity.ok("Update successful");
    }
}
