package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomType type;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    @NotNull
    private Integer capacity;

    private Integer numberOfComputers;

}