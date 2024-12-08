package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.ExamType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("EXAM")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamReservation extends Reservation {

    @NotBlank
    private String subject;

    @NotBlank
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExamType examType;

}