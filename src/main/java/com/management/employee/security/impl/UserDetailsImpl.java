package com.management.employee.security.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.management.employee.employee.entity.Employees;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Provides user details to Spring Security.

@ToString
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Boolean isActive;

    private GrantedAuthority authority;

    public UserDetailsImpl(Long id, String username, String password, Boolean isActive,
                           GrantedAuthority authority) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.isActive = isActive;
    }

    public static UserDetailsImpl build(Employees user) {
        String role = "Admin";                    //assume role is Admin now
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);

        return new UserDetailsImpl(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getIsActive(),
                grantedAuthority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
