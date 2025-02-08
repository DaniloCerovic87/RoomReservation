package com.university.room.reservation.controller;

import com.university.room.reservation.request.CreateUserRequest;
import com.university.room.reservation.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @PostMapping("/createUserForEmployee")
    public ResponseEntity<Void> createUserForEmployee(@Valid @RequestBody CreateUserRequest createUserRequest) {
        adminService.createUserForEmployee(createUserRequest);
        return ResponseEntity.ok().build();
    }
}