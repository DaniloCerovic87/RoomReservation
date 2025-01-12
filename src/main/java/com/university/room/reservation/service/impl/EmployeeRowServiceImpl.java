package com.university.room.reservation.service.impl;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.mapper.EmployeeRowMapper;
import com.university.room.reservation.model.EmployeeRow;
import com.university.room.reservation.model.enums.EmployeeRowStatus;
import com.university.room.reservation.repository.EmployeeRowRepository;
import com.university.room.reservation.service.EmployeeRowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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
    public List<EmployeeRowDTO> parseAndStoreEmployeeRows(MultipartFile file, Long fileId) {
        List<EmployeeRowDTO> importedEmployeeData = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (!areHeadersValid(sheet)) {
                return Collections.emptyList();
            }

            DataFormatter dataFormatter = new DataFormatter();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row)) {
                    log.debug("Row at index {} is empty ", rowIndex);
                    continue;
                }

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

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            String cellValue = getCellValue(row, i, new DataFormatter());

            if (cellValue != null && !cellValue.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean areHeadersValid(Sheet sheet) {
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            log.error("The first row (header) is missing.");
            return false;
        }

        List<String> expectedHeaders = Arrays.asList("firstName", "lastName", "personalId", "email", "title", "department");
        for (int i = 0; i < expectedHeaders.size(); i++) {
            String header = getCellValue(firstRow, i, new DataFormatter());
            if (!expectedHeaders.get(i).equalsIgnoreCase(header)) {
                log.error("Invalid header at column {}. Expected: {} but found: {}", i, expectedHeaders.get(i), header);
                return false;
            }
        }

        return true;
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
