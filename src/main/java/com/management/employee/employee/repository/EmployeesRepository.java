package com.management.employee.employee.repository;

import com.management.employee.employee.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeesRepository extends JpaRepository<Employees, Long> {

    Optional<Employees> findByUserName(String username);

    @Query(value = "call fn_getEmployeeDetailsForUser(:username)", nativeQuery = true)
    Map<String, Object> getEmployeeDetailsForUser(@Param("username") String username);

    Optional<Employees> findByUserNameAndIsActiveTrue(String username);

    @Query(value = "call fn_getEmployeesList(:id, :searchString, :page, :size)", nativeQuery = true)
    List<Map<String, Object>> getEmployeesList(
            @Param("id") Long id,
            @Param("searchString") String searchString,
            @Param("page") Integer page,
            @Param("size") Integer size
    );

    @Query(value = "call fn_getEmployeesListCount(:id, :searchString)", nativeQuery = true)
    Long getEmployeesCount(@Param("id") Long id, @Param("searchString") String searchString);
}
