package com.university.room.reservation.service;

import com.university.room.reservation.request.CreateUserRequest;

public interface AdminService {

    void createUserForEmployee(CreateUserRequest createUserRequest);

}
