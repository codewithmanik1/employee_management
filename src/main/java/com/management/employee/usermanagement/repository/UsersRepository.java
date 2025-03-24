package com.management.employee.usermanagement.repository;

import com.management.employee.employee.entity.Employees;
import com.management.employee.usermanagement.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserName(String username);

    @Query(value = "call fn_getEmployeeDetailsForUser(:username)", nativeQuery = true)
    Map<String, Object> getEmployeeDetailsForUser(@Param("username") String username);


    Optional<Users> findByUserNameAndIsDeleteFalse(String username);
}
