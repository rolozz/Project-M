package com.java.project.userinfoservice.mapper;

import com.java.project.userinfoservice.dto.AccountIdDto;
import com.java.project.userinfoservice.entities.AccountId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountDetailsMapper.class)
public interface AccountIdMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountDetails", source = "accountDetailsDto")
    AccountId toEntity(AccountIdDto accountIdDto);

    @Mapping(target = "accountDetailsDto", source = "accountDetails")
    AccountIdDto toDto(AccountId accountId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountDetails", source = "accountDetailsDto")
    AccountId mergeToEntity(AccountIdDto accountIdDto, @MappingTarget AccountId accountId);

    List<AccountIdDto> toDtoList(List<AccountId> accountIds);
}
