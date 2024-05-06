package com.smartjob.user.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

}
