package com.endava.parkinglot.mapper;

import com.endava.parkinglot.DTO.UserRegistrationDtoRequest;
import com.endava.parkinglot.DTO.UserRegistrationDtoResponse;
import com.endava.parkinglot.enums.Role;
import com.endava.parkinglot.model.UserEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserMapper userMapper;

    @Test
    void shouldReturnEntity() {
        UserRegistrationDtoRequest requestDto = UserRegistrationDtoRequest.builder()
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .build();

        UserEntity expectedEntity = UserEntity.builder()
                 .email("john23@gmail.com")
                 .name("Jonathan")
                 .password("Jonathan_2")
                 .phone("068112233")
                 .build();

        when(modelMapper.map(requestDto, UserEntity.class)).thenReturn(expectedEntity);

        UserEntity returnedEntity = userMapper.mapRequestDtoToEntity(requestDto);
        verify(modelMapper).map(requestDto, UserEntity.class);

        assertAll(
                () -> assertEquals(expectedEntity.getEmail(),returnedEntity.getEmail()),
                () -> assertEquals(expectedEntity.getName(),returnedEntity.getName()),
                () -> assertEquals(expectedEntity.getPassword(),returnedEntity.getPassword()),
                () -> assertEquals(expectedEntity.getPhone(),returnedEntity.getPhone())
        );
    }

    @Test
    void shouldReturnResponseDto() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("john23@gmail.com")
                .name("Jonathan")
                .password("Jonathan_2")
                .phone("068112233")
                .role(Role.REGULAR)
                .build();

        UserRegistrationDtoResponse expectedResponseDto = UserRegistrationDtoResponse.builder()
                .name("Jonathan")
                .email("john23@gmail.com")
                .role(Role.REGULAR)
                .build();

        when(modelMapper.map(userEntity, UserRegistrationDtoResponse.class)).thenReturn(expectedResponseDto);

        UserRegistrationDtoResponse returnedResponseDto = userMapper.mapEntityToResponseDto(userEntity);
        verify(modelMapper).map(userEntity, UserRegistrationDtoResponse.class);

        assertAll(
                () -> assertEquals(expectedResponseDto.getName(), userEntity.getName()),
                () -> assertEquals(expectedResponseDto.getRole(), userEntity.getRole())
        );
    }
}