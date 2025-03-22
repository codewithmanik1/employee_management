package com.management.employee.security.impl;

import com.management.employee.employee.entity.Employees;
import com.management.employee.employee.repository.EmployeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Get our Employees data with Spring Security’s authentication system.

@Service
public class EmployeeUserDetails implements UserDetailsService {
    @Autowired
    private EmployeesRepository employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employees users = employeeRepo.findByUserNameAndIsActiveTrue(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
        System.out.println(users.getId());
        System.out.println(users.getUserName());
        System.out.println(users.getName());
        return UserDetailsImpl.build(users);
    }
}