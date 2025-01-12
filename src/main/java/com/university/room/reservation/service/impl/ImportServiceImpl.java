package com.university.room.reservation.service.impl;

import com.university.room.reservation.dto.EmployeeFileDTO;
import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.model.enums.EmployeeFileStatus;
import com.university.room.reservation.service.EmployeeFileService;
import com.university.room.reservation.service.EmployeeRowService;
import com.university.room.reservation.service.EmployeeService;
import com.university.room.reservation.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final EmployeeFileService employeeFileService;

    private final EmployeeRowService employeeRowService;

    private final EmployeeService employeeService;

    @Override
    @Transactional
    public void importEmployees(MultipartFile file, Long userId) {
        EmployeeFileDTO fileDTO = employeeFileService.validateAndCreateFile(file.getOriginalFilename(), userId);
        Long fileId = fileDTO.getId();
        List<EmployeeRowDTO> employees = employeeRowService.parseAndStoreEmployeeRows(file, fileId);

        if (employees.isEmpty()) {
            employeeFileService.updateEmployeeFileStatus(fileId, EmployeeFileStatus.FAILED);
            return;
        }

        employeeService.processEmployeeImport(employees, fileId);
    }
}
