package com.java.project.userinfoservice.services;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.dto.RequestDto;
import com.java.project.userinfoservice.dto.UpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {

    void save(AccountIdDto accountIdDto);

    void update(String username, AccountIdDto accountIdDto);

    void saveBackUp(RequestDto requestDto);

    void updateBackUp(UpdateDto updateDto);

    AccountIdDto findByName(String username);

    List<AccountIdDto> findListByNames(List<String> usernames );

}
