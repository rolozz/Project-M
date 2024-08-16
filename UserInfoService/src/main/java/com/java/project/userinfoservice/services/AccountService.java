package com.java.project.userinfoservice.services;

import com.java.project.userinfoservice.dto.AccountIdDto;

public interface AccountService {

    void save(AccountIdDto accountIdDto);
    void update(Long id, AccountIdDto accountIdDto);

}
