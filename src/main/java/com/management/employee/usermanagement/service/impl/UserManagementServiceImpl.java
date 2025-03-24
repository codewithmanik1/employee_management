package com.management.employee.usermanagement.service.impl;

import com.management.employee.common.ApiResponse;
import com.management.employee.employee.entity.Employees;
import com.management.employee.employee.repository.EmployeesRepository;
import com.management.employee.security.JwtUtils;
import com.management.employee.usermanagement.dto.request.LoginRequestDto;
import com.management.employee.usermanagement.dto.request.SignUpRequestDto;
import com.management.employee.usermanagement.dto.response.UserResponseDto;
import com.management.employee.usermanagement.entity.Users;
import com.management.employee.usermanagement.repository.UsersRepository;
import com.management.employee.usermanagement.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public ResponseEntity<?> login(LoginRequestDto loginDto) {
        System.out.printf("In Login");
        var response = new ApiResponse<>();

        if (loginDto.getUserName() != null && loginDto.getPassword() != null) {
            Optional<Users> usersInfo = usersRepository.findByUserName(loginDto.getUserName());
            Map<String, Object> userInformation = usersRepository.getEmployeeDetailsForUser(loginDto.getUserName());
            if (usersInfo.isPresent()) {
                if (passwordEncoder.matches(loginDto.getPassword(), usersInfo.get().getPassword())) {
                    System.out.println("Here !!!!");
                    String token = jwtUtils.generateToken(loginDto.getUserName(), usersInfo.get().getId());
                    UserResponseDto userResponse = new UserResponseDto();
                    userResponse.setEmployeeId(Long.valueOf(userInformation.get("employeeId").toString()));
                    userResponse.setUserName(loginDto.getUserName());
                    userResponse.setToken(token);
                    response.responseMethod(HttpStatus.OK.value(), "Login Successfully", userResponse, null);
                }else{
                    System.out.println("In Wrong loop username or password does not matches");
                    response.responseMethod(HttpStatus.NOT_FOUND.value(), "Login Failed", null, null);
                }
            }else{
                response.responseMethod(HttpStatus.NOT_FOUND.value(), "Login Failed", null, null);
            }
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> signUp(SignUpRequestDto signUpRequestDto) {
        var response = new ApiResponse<>();
        Users users = new Users();
        users.setUserName(signUpRequestDto.getUserName());
        users.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        usersRepository.save(users);
        response.responseMethod(HttpStatus.OK.value(), "Account created successfully", null, null);
        return ResponseEntity.ok(response);
    }
}
