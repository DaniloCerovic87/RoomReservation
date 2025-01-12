package com.university.room.reservation.service;

import com.university.room.reservation.dto.EmployeeRowDTO;

import java.util.List;

public interface EmployeeService {
    void processEmployeeImport(List<EmployeeRowDTO> employees, Long fileId);
}
