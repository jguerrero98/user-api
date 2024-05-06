package com.smartjob.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPhoneRequestDto {

    private Long id;
    @JsonProperty("number")
    private String number;
    @JsonProperty("citycode")
    private String cityCode;
    @JsonProperty("countrycode")
    private String countryCode;

}
