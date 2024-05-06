package com.smartjob.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailUserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private Instant created;
    private Instant modified;
    private Instant lastLogin;
    @JsonProperty("is_active")
    private Boolean isActive;
    private List<DetailPhoneResponseDto> phones;
}
