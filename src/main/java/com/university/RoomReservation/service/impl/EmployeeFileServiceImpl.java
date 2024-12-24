package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.EmployeeFileDTO;
import com.university.RoomReservation.dto.UserDTO;
import com.university.RoomReservation.exception.ValidationException;
import com.university.RoomReservation.mapper.EmployeeFileMapper;
import com.university.RoomReservation.mapper.UserMapper;
import com.university.RoomReservation.model.EmployeeFile;
import com.university.RoomReservation.model.enums.EmployeeFileStatus;
import com.university.RoomReservation.repository.EmployeeFileRepository;
import com.university.RoomReservation.service.EmployeeFileService;
import com.university.RoomReservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeFileServiceImpl implements EmployeeFileService {

    private final UserService userService;

    private final EmployeeFileRepository employeeFileRepository;

    @Override
    public List<EmployeeFileDTO> findAll() {
        return employeeFileRepository.findAll().stream().map(EmployeeFileMapper::toDTO).toList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EmployeeFileDTO validateAndCreateFile(String fileName, Long userId) {
        List<EmployeeFile> employeeFiles = employeeFileRepository.findAll();

        if (employeeFiles.stream().anyMatch(f -> EmployeeFileStatus.IN_PROGRESS.equals(f.getEmployeeFileStatus()))) {
            throw new ValidationException("Another import file is currently being processed. " +
                    "Please wait until the operation is complete before proceeding with a new one");
        }

        UserDTO userDTO = userService.getUserById(userId);

        EmployeeFile employeeFile = EmployeeFile.builder()
                .fileName(fileName)
                .employeeFileStatus(EmployeeFileStatus.IN_PROGRESS)
                .user(UserMapper.toEntity(userDTO))
                .build();

        EmployeeFile savedFile = employeeFileRepository.save(employeeFile);
        return EmployeeFileMapper.toDTO(savedFile);
    }

    @Override
    public void updateEmployeeFileStatus(Long fileId, EmployeeFileStatus status) {
        EmployeeFile file = employeeFileRepository.getReferenceById(fileId);
        file.setEmployeeFileStatus(status);
        employeeFileRepository.save(file);
    }
}
