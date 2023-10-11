package com.vmoscalciuc.parkinglot.validators;

import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.vmoscalciuc.parkinglot.model.UserEntity;
import com.vmoscalciuc.parkinglot.model.repository.UserRepository;
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
        Optional<UserEntity> phoneCheck = userRepository.findByPhone(dtoRequest.getPhone());
        if (emailCheck.isPresent()) {
            errors.rejectValue("email", "", "This email is already registered in the system !");
        }
        if (phoneCheck.isPresent()){
            errors.rejectValue("phone", "", "User with this phone number" +
                    " is already registered in the system !");
        }
    }
}
