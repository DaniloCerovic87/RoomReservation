package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.ExamType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("EXAM")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamReservation extends Reservation {

    private String subject;

    private Integer semester;

    @Enumerated(EnumType.STRING)
    private ExamType examType;

}