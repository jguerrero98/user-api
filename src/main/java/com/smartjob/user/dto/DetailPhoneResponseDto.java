package com.smartjob.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DetailPhoneResponseDto {

    private String number;

    private String cityCode;

    private String countryCode;

}
