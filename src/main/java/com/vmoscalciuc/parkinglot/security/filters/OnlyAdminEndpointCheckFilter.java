package com.vmoscalciuc.parkinglot.security.filters;

import com.vmoscalciuc.parkinglot.enums.Role;
import com.vmoscalciuc.parkinglot.exceptions.user.UserNotGrantedToDoActionException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class OnlyAdminEndpointCheckFilter extends OncePerRequestFilter {

    private final List<Pattern> adminEndpoints;

    public OnlyAdminEndpointCheckFilter() {
        this.adminEndpoints = List.of(
                Pattern.compile("^.*/api/parkingLot/[-\\d]+/addUser$"),
                Pattern.compile("^.*/api/parkingLot/[-\\d]+/deleteUser$"),
                Pattern.compile("^.*/api/parkingLot/[-\\d]+/deleteParkingLot$"),
                Pattern.compile("^.*/api/register/grantAdmin$"),
                Pattern.compile("^.*/api/parkingLot/create$"),
                Pattern.compile("^.*/api/parkingLot/update/[-\\d]+$")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String endpoint = request.getRequestURL().toString();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && matchesDynamicPattern(endpoint)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = "";
            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                role = authority.toString();
            }
            role = role.substring(5);

            if (!role.equals(Role.ADMIN.toString()))
                throw new UserNotGrantedToDoActionException("User doesn't have authorities to do this action !");

        }
        filterChain.doFilter(request, response);
    }

    private boolean matchesDynamicPattern(String endpoint) {
        for (Pattern pattern : adminEndpoints) {
            if (pattern.matcher(endpoint).matches()) {
                return true;
            }
        }
        return false;
    }
}
