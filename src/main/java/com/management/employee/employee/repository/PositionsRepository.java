package com.management.employee.employee.repository;

import com.management.employee.employee.entity.Positions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionsRepository extends JpaRepository<Positions, Long> {

}
