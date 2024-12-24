package com.university.RoomReservation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeFileDTO {

    private Long id;
    private LocalDateTime createdAt;
    private String fileName;
    private Long userId;
    private String employeeFileStatus;
}
