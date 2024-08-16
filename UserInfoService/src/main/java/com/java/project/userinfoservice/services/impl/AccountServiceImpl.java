package com.java.project.userinfoservice.services.impl;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import com.java.project.userinfoservice.entities.AccountId;
import com.java.project.userinfoservice.feign.AuthBackUpSave;
import com.java.project.userinfoservice.mapper.AccountIdMapper;
import com.java.project.userinfoservice.repositories.AccountIdRepository;
import com.java.project.userinfoservice.services.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AuthBackUpSave authBackUpSave;
    private final AccountIdRepository accountIdRepository;
    private final AccountIdMapper accountIdMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AuthBackUpSave authBackUpSave, AccountIdRepository accountIdRepository, AccountIdMapper accountIdMapper, PasswordEncoder passwordEncoder) {
        this.authBackUpSave = authBackUpSave;
        this.accountIdRepository = accountIdRepository;
        this.accountIdMapper = accountIdMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void save(AccountIdDto accountIdDto) {
        AccountId saved = accountIdMapper.toEntity(accountIdDto);
        saved.setPassword(passwordEncoder.encode(accountIdDto.getPassword()));
        accountIdRepository.save(saved);
    }

    @Override
    @Transactional
    public void update(Long id, AccountIdDto accountIdDto) {
        AccountId accountId = accountIdRepository.findById(id).orElseThrow(() -> (new RuntimeException("not found")));
        if (accountIdDto.getPassword() != null) {
            accountIdDto.setPassword(passwordEncoder.encode(accountIdDto.getPassword()));
        }
        accountIdRepository.save(accountIdMapper.mergeToEntity(accountIdDto, accountId));
    }

    @Override
    public void saveBackUp(RequestDto requestDto) {
        authBackUpSave.register(requestDto);
    }

    @Override
    public void updateBackUp(UpdateDto updateDto) {
        authBackUpSave.update(updateDto);
    }

}
