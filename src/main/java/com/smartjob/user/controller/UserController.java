package com.smartjob.user.controller;

import com.smartjob.user.dto.*;
import com.smartjob.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController (IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registerUser")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@Validated @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        RegisterUserResponseDto registerUserResponseDto = this.userService.registerUser(registerUserRequestDto);
        return new ResponseEntity<>(registerUserResponseDto, HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> login(@Validated @RequestBody AuthRequest authRequest) {
        JwtResponse jwtResponse = this.userService.login(authRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/findById/{userId}")
    public ResponseEntity<DetailUserResponseDto> findById(@PathVariable("userId") UUID userId) {
        DetailUserResponseDto detailUserResponseDto = this.userService.findById(userId);
        return new ResponseEntity<>(detailUserResponseDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/modifyUser")
    public ResponseEntity<DetailUserResponseDto> modifyUser(@Validated @RequestBody ModifyUserRequestDto modifyUserRequestDto) {
        DetailUserResponseDto detailUserResponseDto = this.userService.modifyUser(modifyUserRequestDto);
        return new ResponseEntity<>(detailUserResponseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteUser/{userId}")
    public ResponseEntity<DeleteUseResponse> deleteUser(@PathVariable("userId") UUID userId) {
        DeleteUseResponse response = this.userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/refreshToken")
    public ResponseEntity<JwtResponse> refreshToken(@Validated @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = this.userService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

}
