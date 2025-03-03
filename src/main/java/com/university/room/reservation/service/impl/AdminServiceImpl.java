package com.university.room.reservation.service.impl;

import com.university.room.reservation.constants.MessageProperties;
import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.model.Employee;
import com.university.room.reservation.model.Reservation;
import com.university.room.reservation.model.User;
import com.university.room.reservation.model.enums.ReservationStatus;
import com.university.room.reservation.model.enums.Role;
import com.university.room.reservation.repository.EmployeeRepository;
import com.university.room.reservation.repository.ReservationRepository;
import com.university.room.reservation.request.CreateUserRequest;
import com.university.room.reservation.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.university.room.reservation.constants.MessageProperties.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final EmployeeRepository employeeRepository;

    private final ReservationRepository reservationRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUserForEmployee(CreateUserRequest createUserRequest) {
        Employee employee = employeeRepository.findById(createUserRequest.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND));

        if (employee.getUser() != null) {
            throw new ValidationException(USER_ALREADY_ASSIGNED);
        }

        User user = User.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .role(Role.fromValue(createUserRequest.getRole()))
                .build();

        employee.setUser(user);
        employeeRepository.save(employee);
    }

    @Override
    public void approveReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.RESERVATION_NOT_FOUND));
        reservation.setReservationStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservation);
    }

    @Override
    public void declineReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageProperties.RESERVATION_NOT_FOUND));
        reservation.setReservationStatus(ReservationStatus.DECLINED);
        reservationRepository.save(reservation);
    }

}
