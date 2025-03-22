package com.management.employee.employee.entity;

import com.management.employee.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employees extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments departmentsId;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Positions positionsId;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;
}
