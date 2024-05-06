package com.smartjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {

    @NotNull(message = "Enviar name")
    @NotEmpty(message = "name no debe enviarse vacio")
    private String name;
    @NotNull(message = "Enviar email")
    @NotEmpty(message = "email no debe enviarse vacio")
    private String email;
    @NotNull(message = "Enviar password")
    @NotEmpty(message = "password no debe enviarse vacio")
    private String password;
    @NotNull(message = "Enviar phones")
    private List<RegisterPhoneRequestDto> phones;

}
