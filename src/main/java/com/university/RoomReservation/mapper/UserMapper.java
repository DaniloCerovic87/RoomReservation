package com.university.RoomReservation.mapper;

import com.university.RoomReservation.dto.UserDTO;
import com.university.RoomReservation.model.Employee;
import com.university.RoomReservation.model.User;
import com.university.RoomReservation.model.enums.Role;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole().name());
        if (user.getEmployee() != null) {
            userDTO.setEmployeeId(user.getEmployee().getId());
        }

        return userDTO;
    }

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        Role role = Role.valueOf(userDTO.getRole());
        user.setRole(role);
        user.setEmployee(Employee.builder().id(userDTO.getId()).build());
        return user;
    }
}