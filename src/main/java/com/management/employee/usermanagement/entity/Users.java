package com.management.employee.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User name can not be null")
    @Column(name = "user_name", unique = true)
    private String userName;

    @NotNull(message = "Password can not be null")
    @Column(name = "password")
    private String password;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "is_delete")
    private Boolean isDelete = false;
}


