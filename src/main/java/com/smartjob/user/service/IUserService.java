package com.smartjob.user.service;

import com.smartjob.user.dto.*;

import java.util.UUID;

public interface IUserService {

    public JwtResponse login(AuthRequest authRequest);
    public DetailUserResponseDto findById(UUID userId);
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto userRequest);
    public DetailUserResponseDto modifyUser(ModifyUserRequestDto userRequest);
    public DeleteUseResponse deleteUser(UUID userId);
    public JwtResponse refreshToken(RefreshTokenRequest refreshToken);

}
