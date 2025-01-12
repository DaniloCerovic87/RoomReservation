package com.university.room.reservation.mapper;

import com.university.room.reservation.dto.RoomDTO;
import com.university.room.reservation.model.Room;
import com.university.room.reservation.model.enums.RoomType;
import com.university.room.reservation.request.RoomRequest;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public static RoomDTO toDto(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .roomType(room.getRoomType().getValue())
                .capacity(room.getCapacity())
                .numberOfComputers(room.getNumberOfComputers())
                .build();
    }

    public static Room toEntity(RoomRequest request) {
        return Room.builder()
                .name(request.getName())
                .roomType(RoomType.fromValue(request.getRoomType()))
                .capacity(request.getCapacity())
                .numberOfComputers(request.getNumberOfComputers())
                .build();
    }

    public static void updateRoomFromRequest(RoomRequest request, Room room) {
        room.setName(request.getName());
        room.setRoomType(RoomType.fromValue(request.getRoomType()));
        room.setCapacity(request.getCapacity());
        room.setNumberOfComputers(room.getRoomType().equals(RoomType.COMPUTER_ROOM) ? request.getNumberOfComputers() : null);
    }
}
