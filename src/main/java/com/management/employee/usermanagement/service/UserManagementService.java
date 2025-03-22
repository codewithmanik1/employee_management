package com.management.employee.usermanagement.service;

import com.management.employee.usermanagement.dto.request.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserManagementService {

    ResponseEntity<?> login(LoginRequestDto loginRequestDto);
}
