package com.management.employee.employee.entity;

import com.management.employee.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mt_departments")
public class Departments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department")
    private String department;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_delete")
    private Boolean isDelete = false;
}
