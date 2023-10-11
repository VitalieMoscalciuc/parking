package com.vmoscalciuc.parkinglot.security;

import com.vmoscalciuc.parkinglot.model.UserEntity;
import com.vmoscalciuc.parkinglot.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user in the system !"));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .authorities(Collections.emptyList())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}
