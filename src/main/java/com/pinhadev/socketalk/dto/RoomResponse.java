package com.pinhadev.socketalk.dto;

import com.pinhadev.socketalk.model.Room;

import java.time.Instant;
import java.util.UUID;

public record RoomResponse(
    UUID id,
    String name,
    String ownerUsername,
    Instant createdAt
) {
    public static RoomResponse fromEntity(Room room) {
        return new RoomResponse(
            room.getId(),
            room.getName(),
            room.getOwner().getUsername(),
            room.getCreatedAt()
        );
    }
}
