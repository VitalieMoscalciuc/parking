package com.endava.parkinglot.util;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationDtoRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDtoRequest dtoRequest = (UserRegistrationDtoRequest) target;

        Optional<UserEntity> emailCheck = userRepository.findByEmail(dtoRequest.getEmail());
        if (emailCheck.isPresent())
            errors.rejectValue("email", "", "This email is already registered in the system !");
    }
}
