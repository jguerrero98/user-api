package com.smartjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AuthRequest {
    @NotNull(message = "Enviar email")
    @NotEmpty(message = "email no debe enviarse vacio")
    private String email;
    @NotNull(message = "Enviar password")
    @NotEmpty(message = "password no debe enviarse vacio")
    private String password;

}
