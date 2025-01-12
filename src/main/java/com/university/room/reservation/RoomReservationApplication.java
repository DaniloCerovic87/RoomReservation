package com.university.room.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RoomReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomReservationApplication.class, args);
    }

}
