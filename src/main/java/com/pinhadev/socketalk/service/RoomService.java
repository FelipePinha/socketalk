package com.pinhadev.socketalk.service;

import com.pinhadev.socketalk.dto.RoomRequest;
import com.pinhadev.socketalk.dto.RoomResponse;
import com.pinhadev.socketalk.exception.ResourceNotFoundException;
import com.pinhadev.socketalk.model.Room;
import com.pinhadev.socketalk.model.User;
import com.pinhadev.socketalk.repository.RoomRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomResponse createRoom(RoomRequest request, User user) {
        var room = Room.builder()
                .name(request.name())
                .owner(user)
                .build();

        roomRepository.save(room);

        return RoomResponse.fromEntity(room);
    }

    public List<RoomResponse> getRooms(String name) {
        List<Room> rooms;

        if(Strings.isNotBlank(name)) {
            rooms = roomRepository.findByNameContainingIgnoreCase(name);

            return rooms.stream().map(RoomResponse::fromEntity).toList();
        }

        rooms = roomRepository.findAll();
        return rooms.stream().map(RoomResponse::fromEntity).toList();
    }

    public RoomResponse getRoomById(UUID id) {
        var room = roomRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Sala não encontrada"));

        return RoomResponse.fromEntity(room);
    }
}
