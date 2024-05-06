package com.smartjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotNull(message = "Enviar token")
    @NotEmpty(message = "token no debe enviarse vacio")
    private String token;

}
