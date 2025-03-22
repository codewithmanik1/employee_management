package com.management.employee.usermanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotNull(message = "user name cannot be null")
    private String userName;

    @NotNull(message = "password cannot be null")
    private String password;
}
