package com.university.room.reservation.service.impl;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.exception.EntityExistsException;
import com.university.room.reservation.exception.InvalidCellContentException;
import com.university.room.reservation.model.Employee;
import com.university.room.reservation.model.enums.EmployeeFileStatus;
import com.university.room.reservation.model.enums.EmployeeRowStatus;
import com.university.room.reservation.repository.EmployeeRepository;
import com.university.room.reservation.service.EmployeeFileService;
import com.university.room.reservation.service.EmployeeRowService;
import com.university.room.reservation.service.EmployeeService;
import com.university.room.reservation.util.EmployeeRowValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRowService employeeRowService;

    private final EmployeeFileService employeeFileService;

    private final EmployeeRepository employeeRepository;

    @Override
    @Async
    public void processEmployeeImport(List<EmployeeRowDTO> employees, Long fileId) {
        for (EmployeeRowDTO employeeRow : employees) {
            Long employeeRowId = employeeRow.getId();
            try {
                employeeRowService.updateEmployeeRowStatus(employeeRowId, EmployeeRowStatus.NEW, null);
                EmployeeRowValidator.validateCellsInputFormat(employeeRow);
                validateIntegrityAndSave(employeeRow);
                employeeRowService.updateEmployeeRowStatus(employeeRowId, EmployeeRowStatus.SUCCESS, null);
            } catch (InvalidCellContentException | EntityExistsException e) {
                employeeRowService.updateEmployeeRowStatus(employeeRowId, EmployeeRowStatus.FAILED, e.getMessage());
            } catch (Exception e) {
                String errorMessage = "Unexpected error occurred while processing employee row:" + employeeRowId;
                log.error(errorMessage, e);
                employeeRowService.updateEmployeeRowStatus(employeeRowId, EmployeeRowStatus.FAILED, errorMessage);
            }
        }
        employeeFileService.updateEmployeeFileStatus(fileId, EmployeeFileStatus.FINISHED);
    }

    private void validateIntegrityAndSave(EmployeeRowDTO employeeRow) {
        if (!isEmailUnique(employeeRow.getEmail())) {
            throw new EntityExistsException("An employee with this email already exists");
        }

        if (!isPersonalIdUnique(employeeRow.getPersonalId())) {
            throw new EntityExistsException("An employee with this personal ID already exists");
        }

        Employee employee = Employee.builder()
                .firstName(employeeRow.getFirstName())
                .lastName(employeeRow.getLastName())
                .personalId(employeeRow.getPersonalId())
                .email(employeeRow.getEmail())
                .title(employeeRow.getTitle())
                .department(employeeRow.getDepartment())
                .build();
        employeeRepository.save(employee);
    }

    private boolean isEmailUnique(String email) {
        return employeeRepository.findByEmail(email).isEmpty();
    }

    private boolean isPersonalIdUnique(String personalId) {
        return employeeRepository.findByPersonalId(personalId).isEmpty();
    }


}
