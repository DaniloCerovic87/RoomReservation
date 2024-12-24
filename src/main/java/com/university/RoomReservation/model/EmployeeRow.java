package com.university.RoomReservation.model;

import com.university.RoomReservation.model.enums.EmployeeRowStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EmployeeRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String personalId;

    private String email;

    private String title;

    private String department;

    private String message;

    @Enumerated(EnumType.STRING)
    private EmployeeRowStatus employeeRowStatus;

    @ManyToOne
    @JoinColumn(name = "employee_file_id")
    @NotNull
    private EmployeeFile employeeFile;

}
