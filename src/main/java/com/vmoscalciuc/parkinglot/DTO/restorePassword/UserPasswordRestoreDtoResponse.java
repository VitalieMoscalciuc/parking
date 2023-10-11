package com.vmoscalciuc.parkinglot.DTO.restorePassword;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordRestoreDtoResponse {
    private String message;
}
