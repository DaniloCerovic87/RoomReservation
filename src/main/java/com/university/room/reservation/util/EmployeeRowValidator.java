package com.university.room.reservation.util;

import com.university.room.reservation.dto.EmployeeRowDTO;
import com.university.room.reservation.exception.InvalidCellContentException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class EmployeeRowValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PERSONAL_ID_REGEX = "^[A-Za-z0-9]{6,20}$";

    public static void validateCellsInputFormat(EmployeeRowDTO employeeRow) {
        validateNonBlank("FirstName", employeeRow.getFirstName());
        validateNonBlank("LastName", employeeRow.getLastName());
        validateNonBlank("Title", employeeRow.getTitle());
        validateNonBlank("Department", employeeRow.getDepartment());
        validateCell("Email", employeeRow.getEmail(), EMAIL_REGEX);
        validateCell("PersonalId", employeeRow.getPersonalId(), PERSONAL_ID_REGEX);
    }

    private static void validateNonBlank(String fieldName, String value) {
        if (StringUtils.isBlank(value)) {
            throw new InvalidCellContentException(fieldName + " is empty or null");
        }
    }

    private static void validateCell(String fieldName, String value, String regex) {
        validateNonBlank(fieldName, value);

        if (regex != null && !value.matches(regex)) {
            throw new InvalidCellContentException(fieldName + " does not match the expected format");
        }
    }
}
