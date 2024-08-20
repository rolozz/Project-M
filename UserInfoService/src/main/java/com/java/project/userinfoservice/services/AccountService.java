package com.java.project.userinfoservice.services;

import com.java.project.userinfoservice.dto.AccountIdDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    ResponseEntity<String> save(AccountIdDto accountIdDto);

    ResponseEntity<String> update(String username, AccountIdDto accountIdDto);

    AccountIdDto findByName(String username);

    List<AccountIdDto> findListByNames(List<String> usernames);

}
