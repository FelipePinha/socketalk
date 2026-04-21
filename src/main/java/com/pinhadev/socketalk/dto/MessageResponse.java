package com.pinhadev.socketalk.dto;

import com.pinhadev.socketalk.model.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    String content,
    String authorUsername,
    Instant sentAt
) {
    public static MessageResponse fromEntity(Message message) {
        return new MessageResponse(
            message.getId(),
            message.getContent(),
            message.getAuthor().getUsername(),
            message.getSentAt()
        );
    }
}
