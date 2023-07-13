package com.endava.parkinglot.security.filters;

import com.endava.parkinglot.exceptions.jwt.JWTInvalidException;
import com.endava.parkinglot.exceptions.user.UserNotGrantedToDoActionException;
import com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo.FilterErrorDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }
        catch (UserNotGrantedToDoActionException e) {
            FilterErrorDetails errorResponse = new FilterErrorDetails(LocalDate.now().toString(), e.getMessage());
            String jsonErrorResponse = convertObjectToJson(errorResponse);

            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonErrorResponse);
        }
        catch (JWTInvalidException e) {
            FilterErrorDetails errorResponse = new FilterErrorDetails(LocalDate.now().toString(), e.getMessage());
            String jsonErrorResponse = convertObjectToJson(errorResponse);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonErrorResponse);
        }
        catch (RuntimeException e) {
            FilterErrorDetails errorResponse = new FilterErrorDetails(LocalDate.now().toString(), e.getMessage());
            String jsonErrorResponse = convertObjectToJson(errorResponse);

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(jsonErrorResponse);
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
