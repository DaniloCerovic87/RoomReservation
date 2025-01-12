package com.university.room.reservation.service;

import com.university.room.reservation.dto.EmployeeFileDTO;
import com.university.room.reservation.model.enums.EmployeeFileStatus;

import java.util.List;

public interface EmployeeFileService {

    List<EmployeeFileDTO> findAll();

    EmployeeFileDTO validateAndCreateFile(String fileName, Long userId);

    void updateEmployeeFileStatus(Long fileId, EmployeeFileStatus status);
}
