package com.management.employee.test;

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
import com.management.employee.employee.service.impl.EmployeeManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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
    private PositionsRepository positionsRepository;

    @Mock
    private EmployeesRepository employeesRepository;

    @InjectMocks
    private EmployeeManagementServiceImpl service;

    private ApiResponse response;

    @BeforeEach
    void setUp() {
        response = new ApiResponse<>();
    }

    // Tests for saveDepartment
    @Test
    void saveDepartment_Success() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentName("HR");
        dto.setIsActive(true);
        dto.setCreatedBy(1L);

        when(departmentsRepository.save(any(Departments.class))).thenReturn(new Departments());

        ResponseEntity<?> result = service.saveDepartment(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data saved successfully", apiResponse.getMessage());
        verify(departmentsRepository, times(1)).save(any(Departments.class));
    }

    // Tests for savePosition
    @Test
    void savePosition_Success() {
        PositionRequestDto dto = new PositionRequestDto();
        dto.setPosition("Developer");
        dto.setIsActive(true);
        dto.setCreatedBy(1L);

        when(positionsRepository.save(any(Positions.class))).thenReturn(new Positions());

        ResponseEntity<?> result = service.savePosition(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data saved successfully", apiResponse.getMessage());
        verify(positionsRepository, times(1)).save(any(Positions.class));
    }

    // Tests for registerEmployee
    @Test
    void registerEmployee_NewEmployee_Success() {
        EmployeesRequestDto dto = new EmployeesRequestDto();
        dto.setName("Maanik Tambulkar");
        dto.setEmail("maanik.speaks@gmail.com");
        dto.setDepartmentsId(new Departments());
        dto.setPositionsId(new Positions());
        dto.setSalary(50000.0);
        dto.setDateOfJoining(LocalDate.now());
        dto.setCreatedBy(1L);
        dto.setLastModifiedBy(1L);

        when(employeesRepository.save(any(Employees.class))).thenReturn(new Employees());

        ResponseEntity<?> result = service.registerEmployee(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Employee saved successfully", apiResponse.getMessage());
        verify(employeesRepository, times(1)).save(any(Employees.class));
    }

    @Test
    void registerEmployee_UpdateEmployee_Success() {
        EmployeesRequestDto dto = new EmployeesRequestDto();
        dto.setId(1L);
        dto.setName("John Doe Updated");
        dto.setEmail("john.updated@example.com");
        dto.setLastModifiedBy(2L);

        Employees existingEmployee = new Employees();
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeesRepository.save(any(Employees.class))).thenReturn(existingEmployee);

        ResponseEntity<?> result = service.registerEmployee(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Employee saved successfully", apiResponse.getMessage());
        verify(employeesRepository, times(1)).save(any(Employees.class));
    }

    // Tests for deleteEmployee
    @Test
    void deleteEmployee_EmployeeExists() {
        Long employeeId = 1L;
        Employees employee = new Employees();
        when(employeesRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeesRepository.save(any(Employees.class))).thenReturn(employee);

        ResponseEntity<?> result = service.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data deleted successfully", apiResponse.getMessage());
        assertTrue(employee.getIsDelete());
        assertFalse(employee.getIsActive());
        verify(employeesRepository, times(1)).save(employee);
    }

    @Test
    void deleteEmployee_EmployeeNotFound() {
        Long employeeId = 1L;
        when(employeesRepository.findById(employeeId)).thenReturn(Optional.empty());

        ResponseEntity<?> result = service.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data is not found for this Id : " + employeeId, apiResponse.getMessage());
        verify(employeesRepository, never()).save(any(Employees.class));
    }

    // Tests for getEmployeesList
    @Test
    void getEmployeesList_Success() {
        FilterDto filterDto = new FilterDto();
        filterDto.setId(1L);
        filterDto.setSearchString("John");
        filterDto.setPage(0);
        filterDto.setSize(10);

        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("id", 1L);
        employeeData.put("name", "John Doe");

        when(employeesRepository.getEmployeesList(1L, "John", 0, 10))
                .thenReturn(Collections.singletonList(employeeData));
        when(employeesRepository.getEmployeesCount(1L, "John")).thenReturn(1L);

        ResponseEntity<?> result = service.getEmployeesList(filterDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data fetch successfully", apiResponse.getMessage());
        assertEquals(Collections.singletonList(employeeData), apiResponse.getResult());
        assertEquals(1L, apiResponse.getCount());
    }

    @Test
    void getEmployeesList_NoData() {
        FilterDto filterDto = new FilterDto();
        filterDto.setId(1L);
        filterDto.setSearchString("John");
        filterDto.setPage(0);
        filterDto.setSize(10);

        when(employeesRepository.getEmployeesList(1L, "John", 0, 10))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> result = service.getEmployeesList(filterDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Data not found", apiResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), apiResponse.getStatusCode());
        assertNull(apiResponse.getResult());
    }

    // Tests for updateEmployee (reuses registerEmployee)
    @Test
    void updateEmployee_Success() {
        EmployeesRequestDto dto = new EmployeesRequestDto();
        dto.setId(1L);
        dto.setName("John Doe Updated");
        dto.setLastModifiedBy(2L);

        Employees existingEmployee = new Employees();
        when(employeesRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeesRepository.save(any(Employees.class))).thenReturn(existingEmployee);

        ResponseEntity<?> result = service.updateEmployee(dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        ApiResponse apiResponse = (ApiResponse) result.getBody();
        assertEquals("Employee saved successfully", apiResponse.getMessage());
        verify(employeesRepository, times(1)).save(any(Employees.class));
    }
}
