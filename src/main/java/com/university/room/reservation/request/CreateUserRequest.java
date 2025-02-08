package com.university.room.reservation.request;

import com.university.room.reservation.constants.MessageProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotNull(message = MessageProperties.EMPLOYEE_ID_NOT_NULL)
    private Long employeeId;

    @NotBlank(message = MessageProperties.ROLE_NOT_BLANK)
    private String role;

    @NotBlank(message = MessageProperties.USERNAME_NOT_BLANK)
    private String username;

    @NotBlank(message = MessageProperties.PASSWORD_NOT_BLANK)
    private String password;

}
