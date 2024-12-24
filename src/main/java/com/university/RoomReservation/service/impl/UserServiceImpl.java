package com.university.RoomReservation.service.impl;

import com.university.RoomReservation.dto.UserDTO;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import com.university.RoomReservation.mapper.UserMapper;
import com.university.RoomReservation.model.User;
import com.university.RoomReservation.repository.UserRepository;
import com.university.RoomReservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.university.RoomReservation.constants.MessageProperties.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        return UserMapper.toDTO(user);
    }

}
