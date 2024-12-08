package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.ClassType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("Class")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassReservation extends Reservation {

    @NotBlank
    private String subject;

    @NotNull
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ClassType classType;

}
