package com.university.room.reservation.controller;

import com.university.room.reservation.service.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Slf4j
public class ImportController {

    private final ImportService importService;

    @PostMapping("/employees")
    public ResponseEntity<Void> importEmployees(@RequestParam("file") MultipartFile file,
                                                @RequestParam("userId") Long userId) {
        log.debug("importEmployees({})", file.getOriginalFilename());
        importService.importEmployees(file, userId);
        return ResponseEntity.ok().build();
    }

}
