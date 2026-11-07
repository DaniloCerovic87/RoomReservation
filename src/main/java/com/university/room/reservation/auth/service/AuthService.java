package com.university.room.reservation.auth.service;

import com.university.room.reservation.auth.request.LoginRequest;
import com.university.room.reservation.auth.response.LoginResponse;
import com.university.room.reservation.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return LoginResponse.builder()
                .token(jwtService.generateToken(request.getUsername()))
                .build();
    }

}
