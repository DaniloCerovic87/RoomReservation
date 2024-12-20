package com.university.RoomReservation.mapper;

import com.university.RoomReservation.model.Room;
import com.university.RoomReservation.model.enums.RoomType;
import com.university.RoomReservation.request.CreateRoomRequest;
import com.university.RoomReservation.request.UpdateRoomRequest;
import com.university.RoomReservation.response.RoomResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public static RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .roomType(room.getRoomType().getValue())
                .capacity(room.getCapacity())
                .numberOfComputers(room.getNumberOfComputers())
                .build();
    }

    public static Room toRoom(CreateRoomRequest request) {
        return Room.builder()
                .name(request.getName())
                .roomType(RoomType.fromValue(request.getRoomType()))
                .capacity(request.getCapacity())
                .numberOfComputers(request.getNumberOfComputers())
                .build();
    }

    public static void updateRoomFromRequest(UpdateRoomRequest request, Room room) {
        room.setName(request.getName());
        room.setRoomType(RoomType.fromValue(request.getRoomType()));
        room.setCapacity(request.getCapacity());
        room.setNumberOfComputers(room.getRoomType().equals(RoomType.COMPUTER_ROOM) ? request.getNumberOfComputers() : null);
    }
}
