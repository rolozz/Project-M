package com.java.project.userinfoservice.services.impl;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import com.java.project.userinfoservice.entities.AccountId;
import com.java.project.userinfoservice.feign.AuthBackUpSave;
import com.java.project.userinfoservice.mapper.AccountIdMapper;
import com.java.project.userinfoservice.repositories.AccountIdRepository;
import com.java.project.userinfoservice.services.AccountService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class AccountServiceImpl implements AccountService {

    private final AuthBackUpSave authBackUpSave;
    private final AccountIdRepository accountIdRepository;
    private final AccountIdMapper accountIdMapper;
    private final PasswordEncoder passwordEncoder;
    private final CircuitBreaker userInfoCircuitBreaker;

    @Autowired
    public AccountServiceImpl(AuthBackUpSave authBackUpSave, AccountIdRepository accountIdRepository, AccountIdMapper accountIdMapper, PasswordEncoder passwordEncoder, CircuitBreaker userInfoCircuitBreaker) {
        this.authBackUpSave = authBackUpSave;
        this.accountIdRepository = accountIdRepository;
        this.accountIdMapper = accountIdMapper;
        this.passwordEncoder = passwordEncoder;
        this.userInfoCircuitBreaker = userInfoCircuitBreaker;
    }

    @Override
    @Transactional
    public ResponseEntity<String> save(AccountIdDto accountIdDto) {
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
                    authBackUpSave.register(new RequestDto(accountIdDto.getUsername(), accountIdDto.getPassword()));
                    return "successful";
                }).withCircuitBreaker(userInfoCircuitBreaker)
                .withFallback(throwable -> ("backup failed - using fallback"))
                .decorate();
        String fallBackResult = decoratedSupplier.get();
        if (fallBackResult.startsWith("backup failed")) {
            return ResponseEntity.status(503).body(fallBackResult);
        }else{
            AccountId saved = accountIdMapper.toEntity(accountIdDto);
            saved.setPassword(passwordEncoder.encode(accountIdDto.getPassword()));
            accountIdRepository.save(saved);
            return ResponseEntity.ok("Ok");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> update(String username, AccountIdDto accountIdDto) {
        AccountIdDto preAccountIdDto = findByName(username);
        Supplier<String> decoratedSupplier = Decorators.ofSupplier(() -> {
                    UpdateDto updateDto = new UpdateDto();
                    updateDto.setPreUpdate(preAccountIdDto.getUsername());
                    updateDto.setUpdate(accountIdDto.getUsername());
                    updateDto.setPassword(accountIdDto.getPassword());
                    authBackUpSave.update(updateDto);
                    return "Update backUp successful";
                }).withCircuitBreaker(userInfoCircuitBreaker)
                .withFallback(throwable -> "Update backUp failed - using fallback")
                .decorate();
        String fallbackResult = decoratedSupplier.get();
        if (fallbackResult.startsWith("Update backUp failed")) {
            return ResponseEntity.status(503).body(fallbackResult);
        }else {
            AccountId accountId =
                    accountIdRepository.findByUsername(username).orElseThrow(() -> (new RuntimeException("not found")));
            if (accountIdDto.getPassword() != null) {
                accountIdDto.setPassword(passwordEncoder.encode(accountIdDto.getPassword()));
            }
            accountIdRepository.save(accountIdMapper.mergeToEntity(accountIdDto, accountId));
            return ResponseEntity.ok("Update successful");
        }
    }

    @Override
    @Transactional
    public AccountIdDto findByName(String username) {
        return accountIdMapper.toDto(
                accountIdRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("empty"))
        );
    }

    @Override
    @Transactional
    public List<AccountIdDto> findListByNames(List<String> usernames) {
        List<AccountId> byNames = accountIdRepository.findByUsernameIn(usernames);
        return accountIdMapper.toDtoList(byNames);
    }
}

