package com.university.room.reservation.service.impl;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.dto.EmployeeFileDTO;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.mapper.EmployeeFileMapper;
import com.university.room.reservation.model.EmployeeFile;
import com.university.room.reservation.model.User;
import com.university.room.reservation.model.enums.EmployeeFileStatus;
import com.university.room.reservation.repository.EmployeeFileRepository;
import com.university.room.reservation.repository.UserRepository;
import com.university.room.reservation.service.EmployeeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeFileServiceImpl implements EmployeeFileService {

    private final EmployeeFileRepository employeeFileRepository;

    private final UserRepository userRepository;

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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.USER_NOT_FOUND));

        EmployeeFile employeeFile = EmployeeFile.builder()
                .fileName(fileName)
                .employeeFileStatus(EmployeeFileStatus.IN_PROGRESS)
                .user(user)
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
