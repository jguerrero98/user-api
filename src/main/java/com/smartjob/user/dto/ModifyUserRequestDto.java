package com.smartjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserRequestDto {

    @NotNull(message = "Enviar id")
    private UUID id;
    @NotNull(message = "Enviar name")
    @NotEmpty(message = "name no debe enviarse vacio")
    private String name;
    @NotNull(message = "Enviar password")
    @NotEmpty(message = "password no debe enviarse vacio")
    private String password;
    private List<ModifyPhoneRequestDto> phones;

}
