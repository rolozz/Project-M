package com.java.project.userinfoservice.mapper;

import com.java.project.userinfoservice.dto.AccountDetailsDto;
import com.java.project.userinfoservice.entities.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {

    @Mapping(target = "id", ignore = true)
    AccountDetails toEntity(AccountDetailsDto accountDetailsDto);

    AccountDetailsDto toDto(AccountDetails accountDetails);

    @Mapping(target = "id", ignore = true)
    AccountDetails mergeToEntity(AccountDetailsDto accountDetailsDto, @MappingTarget AccountDetails accountDetails);

    List<AccountDetailsDto> toDtoList(List<AccountDetails> accountDetails);

}
