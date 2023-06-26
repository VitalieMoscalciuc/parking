package com.endava.parkinglot.util;

import com.endava.parkinglot.security.filters.ExceptionHandlerFilter;
import com.endava.parkinglot.security.filters.JWTAuthenticationFilter;
import com.endava.parkinglot.security.filters.OnlyAdminEndpointCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final OnlyAdminEndpointCheckFilter onlyAdminEndpointCheckFilter;


    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JWTAuthenticationFilter jwtAuthenticationFilter, ExceptionHandlerFilter exceptionHandlerFilter, OnlyAdminEndpointCheckFilter onlyAdminEndpointCheckFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.onlyAdminEndpointCheckFilter = onlyAdminEndpointCheckFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/register/new", "/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(configurer ->
                        configurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(exceptionHandlerFilter, JWTAuthenticationFilter.class)
                    .addFilterAfter(onlyAdminEndpointCheckFilter, JWTAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
