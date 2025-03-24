package com.management.employee.usermanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private String userName;

    private Long employeeId;

    private String token;
}
