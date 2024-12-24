package com.university.RoomReservation.controller;

import com.university.RoomReservation.dto.EmployeeFileDTO;
import com.university.RoomReservation.service.EmployeeFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee-files")
@RequiredArgsConstructor
@Slf4j
public class EmployeeFilesController {

    private final EmployeeFileService employeeFileService;

    @GetMapping
    public ResponseEntity<List<EmployeeFileDTO>> findAllEmployeeFiles() {
        List<EmployeeFileDTO> files = employeeFileService.findAll();
        return ResponseEntity.ok().body(files);
    }

}
