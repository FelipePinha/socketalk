package com.pinhadev.socketalk.controller;

import com.pinhadev.socketalk.dto.RoomRequest;
import com.pinhadev.socketalk.dto.RoomResponse;
import com.pinhadev.socketalk.model.User;
import com.pinhadev.socketalk.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody RoomRequest request,
            @AuthenticationPrincipal User user) {
        var room = roomService.createRoom(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms(@RequestParam(value = "name", required = false) String name) {
        var rooms = roomService.getRooms(name);

        return ResponseEntity.ok().body(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable UUID id) {
        var room = roomService.getRoomById(id);

        return ResponseEntity.ok().body(room);
    }
}
