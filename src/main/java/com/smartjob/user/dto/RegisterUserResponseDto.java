package com.smartjob.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUserResponseDto {

    private UUID id;
    private Instant created;
    private Instant modified;
    private Instant lastLogin;
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("is_active")
    private Boolean isActive;

}
