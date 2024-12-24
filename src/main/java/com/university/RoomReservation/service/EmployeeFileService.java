package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.EmployeeFileDTO;
import com.university.RoomReservation.model.enums.EmployeeFileStatus;

import java.util.List;

public interface EmployeeFileService {

    List<EmployeeFileDTO> findAll();

    EmployeeFileDTO validateAndCreateFile(String fileName, Long userId);

    void updateEmployeeFileStatus(Long fileId, EmployeeFileStatus status);
}
