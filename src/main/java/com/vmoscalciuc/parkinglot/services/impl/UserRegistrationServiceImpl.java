package com.vmoscalciuc.parkinglot.services.impl;

import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.vmoscalciuc.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.vmoscalciuc.parkinglot.DTO.restorePassword.UserPasswordRestoreDtoResponse;
import com.vmoscalciuc.parkinglot.exceptions.email.FailedEmailNotificationException;
import com.vmoscalciuc.parkinglot.exceptions.user.UserNotFoundException;
import com.vmoscalciuc.parkinglot.exceptions.validation.TooManyRequestsException;
import com.vmoscalciuc.parkinglot.exceptions.validation.ValidationCustomException;
import com.vmoscalciuc.parkinglot.mapper.UserMapper;
import com.vmoscalciuc.parkinglot.enums.Role;
import com.vmoscalciuc.parkinglot.model.UserEntity;
import com.vmoscalciuc.parkinglot.model.repository.UserRepository;
import com.vmoscalciuc.parkinglot.services.EmailNotificationService;
import com.vmoscalciuc.parkinglot.services.UserRegistrationService;
import com.vmoscalciuc.parkinglot.util.PasswordGenerator;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static io.github.bucket4j.Bucket.builder;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final Map<String, Bucket> rateLimitMap;

    private final PasswordGenerator passwordGenerator;

    private final EmailNotificationService emailNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    private static final long RESET_INTERVAL = 1;

    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    //reset the buckets
    private void resetRateLimitBuckets() {
        rateLimitMap.clear();
    }

    @Autowired
    public UserRegistrationServiceImpl(UserRepository userRepository,PasswordGenerator passwordGenerator, UserMapper userMapper, PasswordEncoder passwordEncoder, EmailNotificationService emailNotificationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailNotificationService = emailNotificationService;
        this.passwordGenerator = passwordGenerator;
        this.rateLimitMap = new ConcurrentHashMap<>();
    }

    @Override
    @Transactional(rollbackOn = FailedEmailNotificationException.class)
    public UserPasswordRestoreDtoResponse changeUserPasswordAndSendEmail(String email,String remoteAddr) {
        var response = new UserPasswordRestoreDtoResponse();
        if (isRateLimitExceededByIP(remoteAddr) || isRateLimitExceededByEmail(email)) {
            logger.error("Too many request for changing password");
            throw new TooManyRequestsException();
        }

        if(userRepository.findByEmail(email).isEmpty()){
            response.setMessage("If your email is valid, your password will be sent to  your email");
            return response;
        }

            var newPassword = passwordGenerator.generateRandomPassword();
            var encryptedPassword = passwordEncoder.encode(newPassword);

            logger.info("Updating user password...");

            userRepository.updateUserEntityByPassword(encryptedPassword, email);
            emailNotificationService.sendNewPassword(email, newPassword);

            response.setMessage("If your email is valid, your password will be sent to  your email");

            logger.info("Password was successfully changed.");

            return response;
    }

    private boolean isRateLimitExceededByEmail(String email) {
        Bucket userRateLimit = rateLimitMap.computeIfAbsent(email, key -> {
            scheduler.schedule(this::resetRateLimitBuckets, RESET_INTERVAL, TimeUnit.HOURS);
            return createRateLimitBucket();
        });
        return !userRateLimit.tryConsume(1);
    }

    private boolean isRateLimitExceededByIP(String ipAddress) {
        Bucket userRateLimit = rateLimitMap.computeIfAbsent(ipAddress, key -> {
            scheduler.schedule(this::resetRateLimitBuckets, RESET_INTERVAL, TimeUnit.HOURS);
            return createRateLimitBucket();
        });
        return !userRateLimit.tryConsume(1);
    }

    private static Bucket createRateLimitBucket() {
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofHours(1)));
        return builder().addLimit(limit).build();
    }

    @Override
    public UserRegistrationDtoResponse register(UserRegistrationDtoRequest registrationDtoRequest) {
        logger.info("Trying to register new user.");
        UserEntity user;
        user = userMapper.mapRequestDtoToEntity(registrationDtoRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        user.setName(user.getName().trim().replaceAll("\\s+", " "));
        user.setEnabled(true);
        user.setRole(Role.REGULAR);

        UserEntity savedUser = userRepository.save(user);
        logger.info("User was successfully registered in the system.");
        return userMapper.mapEntityToResponseDto(savedUser);
    }

    @Transactional
    public UserEntity grantAdminPermissionsById(Long userId) {
        logger.info("Trying to grant admin permissions to user with id = " + userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User with id = " + userId + " not found in system.");
                    return new UserNotFoundException("User with ID " + userId + " not found.");
                }
        );
        user.setRole(Role.ADMIN);
        logger.info("Role was successfully granted");
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity grantAdminPermissionsByEmail(String email) {
        logger.info("Trying to grant admin permissions to user with email: " + email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User with email: " + email + " not found in system.");
                    return new UserNotFoundException("User with email " + email + " not found.");
                }
        );
        user.setRole(Role.ADMIN);
        logger.info("Role was successfully granted");
        return userRepository.save(user);
    }

    private void validateEmail(String email) {
        logger.info("Validating email to grant admin permissions...");
        if (email != null) {
            if (!Pattern.matches(
                    "^(?=.{5,320}$)(?=[a-zA-Z0-9])[a-zA-Z0-9._!#$%&'*+/=?^`{|}~-]{1,64}@[a-zA-Z0-9.-]{1,255}\\.[a-zA-Z]{1,63}$", email)) {
                Map<String, String> errors = new HashMap<>();
                errors.put("email", "Invalid email. It should be like: 'example@email.com'");
                logger.error("Email is not valid.");
                throw new ValidationCustomException(errors);
            }
        }
        logger.info("Email was successfully validated.");
    }

    @Override
    public void grantAdminPermissions(Long id, String email) {
        validateEmail(email);
        UserEntity entity;
        if (id != null) {
            entity = grantAdminPermissionsById(id);
        } else if (email != null) {
            entity = grantAdminPermissionsByEmail(email);
        } else {
            logger.error("Missing user ID or email.");
            throw new UserNotFoundException("Missing user ID or email.");
        }

        try {
            emailNotificationService.sendNotificationAboutGrantedAdminRole(entity.getEmail());
        } catch (FailedEmailNotificationException failedEmailNotificationException) {
            logger.warn("Email was not sent, admin permissions granted for user: " + entity.getEmail());
        }
    }
}
