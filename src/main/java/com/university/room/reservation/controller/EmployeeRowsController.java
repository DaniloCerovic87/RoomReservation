package com.university.room.reservation.controller;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.service.EmployeeRowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee-rows")
@RequiredArgsConstructor
@Slf4j
public class EmployeeRowsController {

    private final EmployeeRowService employeeRowService;

    @GetMapping("/{fileId}")
    public ResponseEntity<List<EmployeeRowDTO>> findEmployeeRowsByFileId(@PathVariable Long fileId) {
        List<EmployeeRowDTO> files = employeeRowService.findEmployeeRowsByFileId(fileId);
        return ResponseEntity.ok().body(files);
    }

}
