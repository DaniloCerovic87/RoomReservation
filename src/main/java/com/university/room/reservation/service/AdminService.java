package com.university.room.reservation.service;

import com.university.room.reservation.request.CreateUserRequest;

public interface AdminService {

    void createUserForEmployee(CreateUserRequest createUserRequest);

    void approveReservation(Long id);

    void declineReservation(Long id);
}
