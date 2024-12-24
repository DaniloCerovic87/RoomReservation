package com.university.RoomReservation.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportService {
    void importEmployees(MultipartFile file, Long userId);

}
