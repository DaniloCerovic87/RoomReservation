package com.university.RoomReservation.repository;

import com.university.RoomReservation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
