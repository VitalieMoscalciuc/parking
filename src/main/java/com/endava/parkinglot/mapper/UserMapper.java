package com.endava.parkinglot.mapper;

import com.endava.parkinglot.DTO.register.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.register.UserRegistrationDtoResponse;
import com.endava.parkinglot.model.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserEntity mapRequestDtoToEntity(UserRegistrationDtoRequest userRegistrationDtoRequest) {
        return modelMapper.map(userRegistrationDtoRequest, UserEntity.class);
    }

    public UserRegistrationDtoResponse mapEntityToResponseDto(UserEntity user) {
        return modelMapper.map(user, UserRegistrationDtoResponse.class);
    }

}
