package com.smartjob.user.service;
import com.smartjob.user.configuration.UserProperties;
import com.smartjob.user.dto.*;
import com.smartjob.user.entity.RefreshTokenEntity;
import com.smartjob.user.entity.UserEntity;
import com.smartjob.user.mapper.UserEntityMapper;
import com.smartjob.user.repository.PhoneRepository;
import com.smartjob.user.repository.RefreshTokenRepository;
import com.smartjob.user.repository.UserRepository;
import com.smartjob.user.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserProperties userProperties;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(userService, "userProperties", userProperties);
    }

    @Test
    public void testLogin() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("test@test.com", "password");
        UserDetailsImpl userDetails = new UserDetailsImpl(new UserEntity());
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(refreshTokenRepository.findByUserInfo(any())).thenReturn(Collections.emptyList());
        when(refreshTokenRepository.save(any())).thenReturn(new RefreshTokenEntity());

        // Act
        JwtResponse jwtResponse = userService.login(authRequest);

        // Assert
        assertNotNull(jwtResponse);
        assertNotNull(jwtResponse.getAccessToken());
        assertNotNull(jwtResponse.getRefreshToken());
    }

    @Test
    public void testFindById() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.mapperEntityToDetailsResponseDto(userEntity)).thenReturn(new DetailUserResponseDto());

        // Act
        DetailUserResponseDto responseDto = userService.findById(userId);

        // Assert
        assertNotNull(responseDto);
    }

    @Test
    public void testRegisterUser() {
        // Arrange
        RegisterUserRequestDto userRequest = new RegisterUserRequestDto();
        userRequest.setName("Jose Guerrero");
        userRequest.setEmail("jguerrero98developer@gmail.cl");
        userRequest.setPassword("xxX1@dddd");
        userRequest.setPhones(new ArrayList<>());

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequest.getName());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setPhones(new ArrayList<>());
        userEntity.setCreated(Instant.now());
        userEntity.setModified(Instant.now());
        userEntity.setLastLogin(Instant.now());
        userEntity.setIsActive(true);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZ3VlcnJlcm85OGRldmVsb3BlcjJAZ21haWwuY2wiLCJleHAiOjE3MTQ5ODc4MzAsIm5hbWUiOiJKb3NlIn0.YJ8r0Ht-gPkAS6271sjp2j8jepPtdXjh7fSBOpLqMzc");
        jwtResponse.setRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZ3VlcnJlcm85OGRldmVsb3BlcjJAZ21haWwuY2wiLCJleHAiOjE3MTU1OTI0NTAsIm5hbWUiOiJKb3NlIn0.mDOtzNb2nEwth6XP1wPxNF6pC-Jie7Yjo6dsiby1qK4");

        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setId(UUID.randomUUID());
        registerUserResponseDto.setToken(jwtResponse.getAccessToken());
        registerUserResponseDto.setCreated(Instant.now());
        registerUserResponseDto.setModified(Instant.now());
        registerUserResponseDto.setLastLogin(Instant.now());
        registerUserResponseDto.setIsActive(true);
        registerUserResponseDto.setRefreshToken(jwtResponse.getRefreshToken());

        when(userRepository.findOneByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("passwordEncrypt");
        when(userEntityMapper.mapperUserRequestDtoToEntity(userRequest)).thenReturn(userEntity);
        userEntity.setId(UUID.randomUUID());
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(phoneRepository.saveAll(new ArrayList<>())).thenReturn(new ArrayList<>());
        when(refreshTokenRepository.save(new RefreshTokenEntity())).thenReturn(new RefreshTokenEntity());
        when(userEntityMapper.mapperEntityToRegisterResponseDto(any(), any())).thenReturn(registerUserResponseDto);

        // Act
        RegisterUserResponseDto responseDto = userService.registerUser(userRequest);

        // Assert
        assertNotNull(responseDto);
    }

    @Test
    public void testModifyUser() {
        // Arrange
        ModifyUserRequestDto userRequest = new ModifyUserRequestDto();
        userRequest.setId(UUID.randomUUID());
        userRequest.setName("Jose Guerrero");
        userRequest.setPassword("xX14@ddddd");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userRequest.getId());
        userEntity.setModified(Instant.now());
        userEntity.setName(userRequest.getName());

        when(userRepository.findByIdAndIsActiveTrue(any(UUID.class))).thenReturn(Optional.of(userEntity));
        when(userEntityMapper.mapperEntityToDetailsResponseDto(any())).thenReturn(new DetailUserResponseDto());

        // Act
        DetailUserResponseDto responseDto = userService.modifyUser(userRequest);

        // Assert
        assertNotNull(responseDto);
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        // Act
        DeleteUseResponse response = userService.deleteUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals("Usuario eliminado", response.getMessage());
    }

    @Test
    public void testRefreshToken() {
        // Arrange
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refreshToken");
        UserEntity userEntity = new UserEntity();
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setExpiryDate(Instant.now().plus(3, ChronoUnit.MINUTES));
        refreshTokenEntity.setUserInfo(userEntity);
        when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(refreshTokenEntity));
        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(new RefreshTokenEntity());

        // Act
        JwtResponse response = userService.refreshToken(refreshTokenRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getRefreshToken());
    }

}
