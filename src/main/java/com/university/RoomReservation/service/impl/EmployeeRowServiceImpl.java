package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.EmployeeRowDTO;
import com.university.RoomReservation.mapper.EmployeeRowMapper;
import com.university.RoomReservation.model.EmployeeRow;
import com.university.RoomReservation.model.enums.EmployeeRowStatus;
import com.university.RoomReservation.repository.EmployeeRowRepository;
import com.university.RoomReservation.service.EmployeeRowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeRowServiceImpl implements EmployeeRowService {

    private final EmployeeRowRepository employeeRowRepository;

    @Override
    public List<EmployeeRowDTO> findEmployeeRowsByFileId(Long fileId) {
        List<EmployeeRow> rows = employeeRowRepository.findByEmployeeFileId(fileId);
        return rows.stream().map(EmployeeRowMapper::toDto).toList();
    }

    @Override
    public List<EmployeeRowDTO> parseAndStoreAccessPointRows(MultipartFile file, Long fileId) {
        List<EmployeeRowDTO> importedEmployeeData = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                EmployeeRowDTO employeeRowDTO = EmployeeRowDTO.builder()
                        .firstName(getCellValue(row, 0, dataFormatter))
                        .lastName(getCellValue(row, 1, dataFormatter))
                        .personalId(getCellValue(row, 2, dataFormatter))
                        .email(getCellValue(row, 3, dataFormatter))
                        .title(getCellValue(row, 4, dataFormatter))
                        .department(getCellValue(row, 5, dataFormatter))
                        .employeeRowStatus(EmployeeRowStatus.NEW.getName())
                        .employeeFileId(fileId)
                        .build();
                importedEmployeeData.add(employeeRowDTO);
            }

            List<EmployeeRow> employeeRows = employeeRowRepository.saveAll(importedEmployeeData.stream()
                    .map(EmployeeRowMapper::toEntity).toList());
            return employeeRows.stream().map(EmployeeRowMapper::toDto).toList();
        } catch (Exception e) {
            log.error("An error occurred while trying to parse the excel file: {}", file.getOriginalFilename(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public void updateEmployeeRowStatus(Long rowId, EmployeeRowStatus status, String message) {
        EmployeeRow row = employeeRowRepository.getReferenceById(rowId);
        row.setEmployeeRowStatus(status);
        row.setMessage(message);
        employeeRowRepository.save(row);
    }

    private String getCellValue(Row row, int cellIndex, DataFormatter dataFormatter) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? dataFormatter.formatCellValue(cell) : null;
    }

}
