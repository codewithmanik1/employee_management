package com.management.employee.employee.service.impl;

import com.management.employee.common.ApiResponse;
import com.management.employee.common.FilterDto;
import com.management.employee.employee.dto.request.DepartmentRequestDto;
import com.management.employee.employee.dto.request.EmployeesRequestDto;
import com.management.employee.employee.entity.Departments;
import com.management.employee.employee.entity.Employees;
import com.management.employee.employee.repository.DepartmentsRepository;
import com.management.employee.employee.repository.EmployeesRepository;
import com.management.employee.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeManagementServiceImplTest {

    @Mock
    private DepartmentsRepository departmentsRepository;

    @Mock
    private EmployeesRepository employeesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private EmployeeManagementServiceImpl service;

    private ApiResponse response;

    @BeforeEach
    void setUp() {
        response = new ApiResponse<>();
    }

    @Test
    void saveDepartment_Success() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentName("IT");
        dto.setIsActive(true);
        dto.setCreatedBy(1L);

        when(departmentsRepository.save(any(Departments.class))).thenReturn(new Departments());
        ResponseEntity<ApiResponse> result = (ResponseEntity<ApiResponse>) service.saveDepartment(dto);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Data saved successfully", result.getBody().getMessage());
        verify(departmentsRepository, times(1)).save(any(Departments.class));
    }

    @Test
    void registerEmployee_Success() {
        EmployeesRequestDto dto = new EmployeesRequestDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setUserName("johndoe");
        dto.setPassword("password123");
        dto.setCreatedBy(1L);
        dto.setLastModifiedBy(1L);

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(employeesRepository.save(any(Employees.class))).thenReturn(new Employees());

        ResponseEntity<ApiResponse> result = (ResponseEntity<ApiResponse>) service.registerEmployee(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee saved successfully", result.getBody().getMessage());
        System.out.println(result.getBody().getResult());
        System.out.println(result.getBody().getMessage());
        verify(employeesRepository, times(1)).save(any(Employees.class));
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    void deleteEmployee_EmployeeExists() {
        Long employeeId = 1L;
        Employees employee = new Employees();
        employee.setId(employeeId);
        when(employeesRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<ApiResponse> result = (ResponseEntity<ApiResponse>) service.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Data deleted successfully", result.getBody().getMessage());
        verify(employeesRepository, times(1)).save(employee);
        assertTrue(employee.getIsDelete());
        assertFalse(employee.getIsActive());
    }

    @Test
    void getEmployeesList_Success() {
        FilterDto filterDto = new FilterDto();
        filterDto.setId(1L);
        filterDto.setSearchString(null);
        filterDto.setPage(0);
        filterDto.setSize(10);

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("employeeId", 1L);
        employeeData.put("employeeName", null);
        when(employeesRepository.getEmployeesList(1L, null, 0, 10))
                .thenReturn(Collections.singletonList(employeeData));

        when(employeesRepository.getEmployeesCount(1L, null)).thenReturn(1L);

        ResponseEntity<ApiResponse> result = (ResponseEntity<ApiResponse>) service.getEmployeesList(filterDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Data fetch successfully", result.getBody().getMessage());
    }
}