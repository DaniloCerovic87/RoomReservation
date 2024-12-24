package com.university.RoomReservation.service;

import com.university.RoomReservation.dto.EmployeeRowDTO;

import java.util.List;

public interface EmployeeService {
    void processEmployeeImport(List<EmployeeRowDTO> employees, Long fileId);
}
