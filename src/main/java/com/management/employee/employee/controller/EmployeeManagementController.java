package com.management.employee.employee.controller;

import com.management.employee.common.FilterDto;
import com.management.employee.employee.dto.request.DepartmentRequestDto;
import com.management.employee.employee.dto.request.EmployeesRequestDto;
import com.management.employee.employee.dto.request.PositionRequestDto;
import com.management.employee.employee.service.EmployeeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee_management")
public class EmployeeManagementController {

    @Autowired
    private EmployeeManagementService employeeManagementService;

    @PostMapping("/saveDepartment")
    public ResponseEntity<?> saveDepartment(@RequestBody DepartmentRequestDto departmentRequestDto){
        return employeeManagementService.saveDepartment(departmentRequestDto);
    }

    @PostMapping("/savePosition")
    public ResponseEntity<?> savePosition(@RequestBody PositionRequestDto positionRequestDto){
        return employeeManagementService.savePosition(positionRequestDto);
    }

    @PostMapping("/registerEmployee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeesRequestDto employeesRequestDto){
        return employeeManagementService.registerEmployee(employeesRequestDto);
    }

    @DeleteMapping("/deleteEmployee/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId){
        return employeeManagementService.deleteEmployee(employeeId);
    }

    @PostMapping("/getEmployeesList")
    public ResponseEntity<?> getEmployeesList(@RequestBody FilterDto filterDto){
        return employeeManagementService.getEmployeesList(filterDto);
    }

    @PutMapping("/updateEmployee}")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeesRequestDto employeesRequestDto){
        return employeeManagementService.updateEmployee(employeesRequestDto);
    }
}
