package com.management.employee.usermanagement.controller;

import com.management.employee.usermanagement.dto.request.LoginRequestDto;
import com.management.employee.usermanagement.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(allowedHeaders = {"Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class UsersInfoController {

    @Autowired
    private UserManagementService userManagementService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginDto) {
        return userManagementService.login(loginDto);
    }

}
