package com.university.room.reservation.model;

import com.university.room.reservation.model.enums.ClassType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("CLASS")
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassReservation extends Reservation {

    private String subject;

    private Integer semester;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

}
