package com.endava.parkinglot.util;

import com.endava.parkinglot.enums.Role;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCreationUtils {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void clearAll(){
        userRepository.deleteAll();
    }

    @Transactional
    public UserEntity createAdminUser() {
        UserEntity adminUser = new UserEntity();
        adminUser.setId(1L);
        adminUser.setName("John");
        adminUser.setPassword("John12!");
        adminUser.setEmail("john@greenmail.com");
        adminUser.setPhone("069712432");
        adminUser.setEnabled(true);
        adminUser.setRole(Role.ADMIN);

        return userRepository.save(adminUser);
    }

    @Transactional
    public UserEntity createRegularUser() {
        UserEntity regularUser = new UserEntity();
        regularUser.setId(2L);
        regularUser.setName("Patrick");
        regularUser.setPassword("Patrick12!");
        regularUser.setEmail("test123@gmail.com");
        regularUser.setPhone("061722432");
        regularUser.setEnabled(true);
        regularUser.setRole(Role.REGULAR);

        return userRepository.save(regularUser);
    }
}
