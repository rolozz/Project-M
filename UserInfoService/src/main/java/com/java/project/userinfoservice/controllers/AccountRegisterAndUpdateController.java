package com.java.project.userinfoservice.controllers;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import com.java.project.userinfoservice.entities.AccountId;
import com.java.project.userinfoservice.repositories.AccountIdRepository;
import com.java.project.userinfoservice.services.AccountService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import org.mapstruct.ap.internal.model.Decorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
public class AccountRegisterAndUpdateController {

    private final AccountService accountService;
    private final AccountIdRepository accountIdRepository;
    private final CircuitBreaker userInfoCircuitBreaker;

    @Autowired
    public AccountRegisterAndUpdateController(AccountService accountService, AccountIdRepository accountIdRepository, CircuitBreaker userInfoCircuitBreaker) {
        this.accountService = accountService;
        this.accountIdRepository = accountIdRepository;
        this.userInfoCircuitBreaker = userInfoCircuitBreaker;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AccountIdDto accountIdDto){
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
            accountService.saveBackUp(new RequestDto(accountIdDto.getUsername(), accountIdDto.getPassword()));
            return "successful";
        }).withCircuitBreaker(userInfoCircuitBreaker)
                        .withFallback(throwable -> ("backup failed - using fallback"))
                                .decorate();
        String fallBackResult = decoratedSupplier.get();
        if (fallBackResult.startsWith("backup failed")){
            return ResponseEntity.status(503).body(fallBackResult);
        }
        accountService.save(accountIdDto);
        return ResponseEntity.ok("Ok");
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody AccountIdDto accountIdDto) {
        AccountId accountId = accountIdRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("empty"));
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
                    UpdateDto updateDto = new UpdateDto();
                    updateDto.setPreUpdate(accountId.getUsername());
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
        accountService.update(id, accountIdDto);
        return ResponseEntity.ok("Update successful");
    }
}
