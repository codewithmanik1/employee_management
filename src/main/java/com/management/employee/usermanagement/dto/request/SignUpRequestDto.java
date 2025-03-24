package com.management.employee.usermanagement.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

}
