package com.university.room.reservation.controller;

import com.university.room.reservation.dto.EmployeeFileDTO;
import com.university.room.reservation.service.EmployeeFileService;
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
public class EmployeeFileController {

    private final EmployeeFileService employeeFileService;

    @GetMapping
    public ResponseEntity<List<EmployeeFileDTO>> findAllEmployeeFiles() {
        List<EmployeeFileDTO> files = employeeFileService.findAll();
        return ResponseEntity.ok().body(files);
    }

}
