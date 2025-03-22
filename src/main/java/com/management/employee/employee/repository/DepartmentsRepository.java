package com.management.employee.employee.repository;

import com.management.employee.employee.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentsRepository extends JpaRepository<Departments, Long> {
}
