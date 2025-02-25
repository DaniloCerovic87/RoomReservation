package com.university.room.reservation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRowDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String personalId;

    private String email;

    private String title;

    private String department;

    private String employeeRowStatus;

    private String message;

    private Long employeeFileId;

}
