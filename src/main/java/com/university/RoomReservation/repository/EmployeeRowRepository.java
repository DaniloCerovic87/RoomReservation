package com.university.RoomReservation.repository;

import com.university.RoomReservation.model.EmployeeRow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRowRepository extends JpaRepository<EmployeeRow, Long> {
    List<EmployeeRow> findByEmployeeFileId(Long employeeFileId);

}
