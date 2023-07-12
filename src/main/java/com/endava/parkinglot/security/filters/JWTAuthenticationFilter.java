package com.endava.parkinglot.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.endava.parkinglot.exceptions.jwt.JWTInvalidException;
import com.endava.parkinglot.security.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final List<RequestMatcher> publicEndpoints = Arrays.asList(
            new AntPathRequestMatcher("/api/register/new"),
            new AntPathRequestMatcher("/auth/**"),
            new AntPathRequestMatcher("/api/restore")
    );

    @Autowired
    public JWTAuthenticationFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");

        if (this.publicEndpoints.stream().anyMatch(matcher -> matcher.matches(request))) {
            filterChain.doFilter(request, response);
            return;
        } else if (authHeader == null || (authHeader.contains("Bearer") && authHeader.length() < 7) || authHeader.isBlank()) {
            throw new JWTInvalidException("Invalid JWT Token in Authorization header! It is blank.");
        }

        String jwt = authHeader.substring(7);

        try {
            String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        catch (JWTVerificationException ex) {
            throw new JWTInvalidException("Invalid JWT Token! Your token has not passed validation.", ex);
        }

        filterChain.doFilter(request, response);

    }
}
