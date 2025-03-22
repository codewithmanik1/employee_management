package com.management.employee.security.filter;

import com.management.employee.security.JwtUtils;
import com.management.employee.security.impl.EmployeeUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Handles unauthorized access.
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmployeeUserDetails userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("request " + request.getRequestURI());
        String jwt = request.getHeader("Authorization");
        System.out.println("Filter token " + jwt);

        //if ((jwt != null && jwt.startsWith("Bearer "))) {
            if ((jwt != null)) {
                // Remove the "Bearer " prefix and any extra spaces
            jwt = jwt.substring(6).trim();
            System.out.println("Trimmed Token: [" + jwt + "]");

                // Validate the token
                if (jwtUtils.validateJwtToken(jwt)) {
                    try {
                        String username = jwtUtils.getUsernameFromToken(jwt);
                        System.out.println("Filter username " + username);
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        context.setAuthentication(authentication);
                        SecurityContextHolder.setContext(context);
                    } catch (Exception e) {
                        logger.error("Cannot set user authentication: {}", e);
                    }
                }
        //    }
        }

        filterChain.doFilter(request, response);
    }

}

