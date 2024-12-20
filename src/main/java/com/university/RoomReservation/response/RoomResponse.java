package com.university.RoomReservation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private Long id;

    private String name;

    private String roomType;

    private Integer capacity;

    private Integer numberOfComputers;

}
