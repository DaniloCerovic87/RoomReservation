package com.university.room.reservation.service;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.model.enums.EmployeeRowStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeRowService {

    List<EmployeeRowDTO> findEmployeeRowsByFileId(Long fileId);

    List<EmployeeRowDTO> parseAndStoreEmployeeRows(MultipartFile file, Long fileId);

    void updateEmployeeRowStatus(Long rowId, EmployeeRowStatus status, String message);
}
