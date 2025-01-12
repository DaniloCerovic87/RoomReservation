package com.university.room.reservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Long id;

    private String name;

    private String roomType;

    private Integer capacity;

    private Integer numberOfComputers;

}
