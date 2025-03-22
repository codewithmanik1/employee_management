package com.management.employee.employee.dto.request;

import com.management.employee.employee.entity.Departments;
import com.management.employee.employee.entity.Positions;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeesRequestDto {

    private Long id;

    private String name;

    private String email;

    private Departments departmentsId;

    private Positions positionsId;

    private Double salary;

    private LocalDate dateOfJoining;

    private Boolean isActive = true;

    private Long createdBy;
    private Long lastModifiedBy;

    private String userName;

    private String password;
}
