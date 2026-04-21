package com.pinhadev.socketalk.dto;

import jakarta.validation.constraints.NotBlank;

public record RoomRequest(
    @NotBlank
    String name
) {
}
