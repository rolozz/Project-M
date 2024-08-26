package com.java.project.messageservice.mapper;

import com.java.project.messageservice.dto.ChatMessageDto;
import com.java.project.messageservice.model.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    ChatMessage toEntity(ChatMessageDto dto);

    ChatMessageDto toDto(ChatMessage entity);

    List<ChatMessageDto> toDto(List<ChatMessage> entities);
}
