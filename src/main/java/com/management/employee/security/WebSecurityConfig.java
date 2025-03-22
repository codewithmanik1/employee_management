package com.management.employee.security;

import com.management.employee.security.exception.AuthEntryPointJwt;
import com.management.employee.security.filter.AuthTokenFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


//Ties Spring Security (spring-boot-starter-security) with JWT (jjwt-*) to secure our app.

@EnableWebSecurity     // Activates Spring Security’s web security features.
@Configuration         // this is Spring configuration class.
@EnableMethodSecurity   // Enables method-level security
public class WebSecurityConfig {

    //This endpoints don’t require authentication.
    public static final List<String> AUTH_WHITELIST = List.of(
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/api/public/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.jpeg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/**/*.ttf",
            "/**/*.pdf",
            "/api/auth/login",
            "/api/auth/save/**",
            "/api/devices/save/**");

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] str = AUTH_WHITELIST.toArray(new String[0]);
        http.csrf(AbstractHttpConfigurer::disable)                                                  //using stateless JWT authentication (no sessions).
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())  //allows cross-origin request from frontend app
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(str).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/save/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/devices/save/**").permitAll()
                        //.requestMatchers("/**").permitAll()           // if code is comment don't permit all api without token
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))      //handles unauthorized access (e.g., invalid token).
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);       //for our custom AuthTokenFilter to validate JWTs.
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION,HttpHeaders.CONTENT_TYPE,HttpHeaders.ACCEPT));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //for password hashing.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    //for swagger, instead of postman
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}