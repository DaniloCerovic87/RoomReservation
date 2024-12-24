package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.EmployeeRowDTO;
import com.university.RoomReservation.model.enums.EmployeeRowStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeRowService {

    List<EmployeeRowDTO> findEmployeeRowsByFileId(Long fileId);

    List<EmployeeRowDTO> parseAndStoreAccessPointRows(MultipartFile file, Long fileId);

    void updateEmployeeRowStatus(Long rowId, EmployeeRowStatus status, String message);
}
