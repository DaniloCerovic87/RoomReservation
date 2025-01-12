package com.university.room.reservation.mapper;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.model.EmployeeFile;
import com.university.room.reservation.model.EmployeeRow;
import com.university.room.reservation.model.enums.EmployeeRowStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeRowMapper {

    public static EmployeeRowDTO toDto(EmployeeRow employeeRow) {

        return EmployeeRowDTO.builder()
                .id(employeeRow.getId())
                .firstName(employeeRow.getFirstName())
                .lastName(employeeRow.getLastName())
                .personalId(employeeRow.getPersonalId())
                .email(employeeRow.getEmail())
                .title(employeeRow.getTitle())
                .department(employeeRow.getDepartment())
                .message(employeeRow.getMessage())
                .employeeRowStatus(employeeRow.getEmployeeRowStatus().name())
                .employeeFileId(employeeRow.getEmployeeFile().getId())
                .build();
    }

    public static EmployeeRow toEntity(EmployeeRowDTO employeeRowDTO) {
        return EmployeeRow.builder()
                .id(employeeRowDTO.getId())
                .firstName(employeeRowDTO.getFirstName())
                .lastName(employeeRowDTO.getLastName())
                .personalId(employeeRowDTO.getPersonalId())
                .email(employeeRowDTO.getEmail())
                .title(employeeRowDTO.getTitle())
                .department(employeeRowDTO.getDepartment())
                .message(employeeRowDTO.getMessage())
                .employeeRowStatus(EmployeeRowStatus.valueOf(employeeRowDTO.getEmployeeRowStatus()))
                .employeeFile(EmployeeFile.builder().id(employeeRowDTO.getEmployeeFileId()).build())
                .build();
    }

}
