package com.management.employee.employee.service.impl;

import com.management.employee.common.ApiResponse;
import com.management.employee.common.FilterDto;
import com.management.employee.employee.dto.request.DepartmentRequestDto;
import com.management.employee.employee.dto.request.EmployeesRequestDto;
import com.management.employee.employee.dto.request.PositionRequestDto;
import com.management.employee.employee.entity.Departments;
import com.management.employee.employee.entity.Employees;
import com.management.employee.employee.entity.Positions;
import com.management.employee.employee.repository.DepartmentsRepository;
import com.management.employee.employee.repository.EmployeesRepository;
import com.management.employee.employee.repository.PositionsRepository;
import com.management.employee.employee.service.EmployeeManagementService;
import com.management.employee.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public ResponseEntity<?> saveDepartment(DepartmentRequestDto departmentRequestDto) {
        System.out.printf("In save Department");
        var response = new ApiResponse<>();

        Departments departments = new Departments();
        departments.setDepartment(departmentRequestDto.getDepartmentName());
        departments.setIsActive(departmentRequestDto.getIsActive());
        departments.setCreatedBy(departmentRequestDto.getCreatedBy());
        departments.setLastModifiedBy(departmentRequestDto.getCreatedBy());

        departmentsRepository.save(departments);

        response.responseMethod(HttpStatus.OK.value(), "Data saved successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> savePosition(PositionRequestDto positionRequestDto) {
        var response = new ApiResponse<>();

        Positions positions = new Positions();
        positions.setPositions(positionRequestDto.getPosition());
        positions.setIsActive(positionRequestDto.getIsActive());
        positions.setCreatedBy(positionRequestDto.getCreatedBy());
        positions.setLastModifiedBy(positionRequestDto.getCreatedBy());
        positionsRepository.save(positions);

        response.responseMethod(HttpStatus.OK.value(), "Data saved successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> registerEmployee(EmployeesRequestDto employeesRequestDto) {
        var response = new ApiResponse<>();

        Employees employees = new Employees();

        if(employeesRequestDto.getId() == null){
            employees.setCreatedBy(employeesRequestDto.getCreatedBy());
            employees.setLastModifiedBy(employeesRequestDto.getLastModifiedBy());
        }else{
            employees = employeesRepository.findById(employeesRequestDto.getId()).get();
            employees.setCreatedBy(employeesRequestDto.getLastModifiedBy());
            employees.setLastModifiedDate(LocalDateTime.now());
        }
        employees.setName(employeesRequestDto.getName());
        employees.setEmail(employeesRequestDto.getEmail());
        employees.setDepartmentsId(employeesRequestDto.getDepartmentsId());
        employees.setPositionsId(employeesRequestDto.getPositionsId());
        employees.setSalary(employeesRequestDto.getSalary());
        employees.setDateOfJoining(employeesRequestDto.getDateOfJoining());

        //set Username password
        employees.setUserName(employeesRequestDto.getUserName());
        employees.setPassword(passwordEncoder.encode(employeesRequestDto.getPassword()));
        employeesRepository.save(employees);

        response.responseMethod(HttpStatus.OK.value(), "Employee saved successfully", null, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> deleteEmployee(Long employeeId) {
        var response = new ApiResponse<>();

        Optional<Employees> getData = employeesRepository.findById(employeeId);
        if(getData.isPresent()){
            Employees employees = new Employees();
            employees = getData.get();
            employees.setIsDelete(true);
            employees.setIsActive(false);
            employeesRepository.save(employees);
            response.responseMethod(HttpStatus.OK.value(), "Data deleted successfully", null, null);
        }else{
            response.responseMethod(HttpStatus.OK.value(), "Data is not found for this Id : " + employeeId, null, null);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getEmployeesList(FilterDto filterDto) {
        var response = new ApiResponse<>();
        List<Map<String, Object>> employeeData = employeesRepository.getEmployeesList(filterDto.getId(), filterDto.getSearchString(), filterDto.getPage(), filterDto.getSize());
        if(!employeeData.isEmpty()){
            Long getCount = employeesRepository.getEmployeesCount(filterDto.getId(), filterDto.getSearchString());
            System.out.printf("Employee : " + getCount);
            response.responseMethod(HttpStatus.OK.value(), "Data fetch successfully", employeeData, getCount);
        }else{
            response.responseMethod(HttpStatus.NOT_FOUND.value(), "Data not found", null, null);
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateEmployee(EmployeesRequestDto employeesRequestDto) {
        return registerEmployee(employeesRequestDto);
    }
}

