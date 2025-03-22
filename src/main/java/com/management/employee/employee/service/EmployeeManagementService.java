package com.management.employee.employee.service;

import com.management.employee.common.FilterDto;
import com.management.employee.employee.dto.request.DepartmentRequestDto;
import com.management.employee.employee.dto.request.EmployeesRequestDto;
import com.management.employee.employee.dto.request.PositionRequestDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeManagementService {

    ResponseEntity<?> saveDepartment(DepartmentRequestDto departmentRequestDto);

    ResponseEntity<?> savePosition(PositionRequestDto positionRequestDto);

    ResponseEntity<?> registerEmployee(EmployeesRequestDto employeesRequestDto);

    ResponseEntity<?> deleteEmployee(Long employeeId);

    ResponseEntity<?> getEmployeesList(FilterDto filterDto);

    ResponseEntity<?> updateEmployee(EmployeesRequestDto employeesRequestDto);
}
