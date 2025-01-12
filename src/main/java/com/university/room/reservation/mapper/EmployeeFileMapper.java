package com.university.room.reservation.mapper;

import com.university.room.reservation.dto.EmployeeFileDTO;
import com.university.room.reservation.model.EmployeeFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeFileMapper {

    public static EmployeeFileDTO toDTO(EmployeeFile employeeFile) {
        return EmployeeFileDTO.builder()
                .id(employeeFile.getId())
                .createdAt(employeeFile.getCreatedAt())
                .fileName(employeeFile.getFileName())
                .employeeFileStatus(employeeFile.getEmployeeFileStatus().name())
                .userId(employeeFile.getUser().getId())
                .build();
    }

}