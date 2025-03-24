package com.management.employee.security.impl;

import com.management.employee.employee.entity.Employees;
import com.management.employee.employee.repository.EmployeesRepository;
import com.management.employee.usermanagement.entity.Users;
import com.management.employee.usermanagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Get our Employees data with Spring Security’s authentication system.

@Service
public class EmployeeUserDetails implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUserNameAndIsDeleteFalse(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
        System.out.println(users.getId());
        System.out.println(users.getUserName());
        return UserDetailsImpl.build(users);
    }
}