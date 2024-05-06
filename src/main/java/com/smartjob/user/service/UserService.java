package com.smartjob.user.service;

import com.smartjob.user.configuration.UserProperties;
import com.smartjob.user.dto.*;
import com.smartjob.user.entity.PhoneEntity;
import com.smartjob.user.entity.RefreshTokenEntity;
import com.smartjob.user.entity.UserEntity;
import com.smartjob.user.exception.BusinessException;
import com.smartjob.user.mapper.UserEntityMapper;
import com.smartjob.user.repository.PhoneRepository;
import com.smartjob.user.repository.RefreshTokenRepository;
import com.smartjob.user.repository.UserRepository;
import com.smartjob.user.util.TokenUtils;
import com.smartjob.user.security.UserDetailsImpl;
import com.smartjob.user.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;

    private PhoneRepository phoneRepository;

    private RefreshTokenRepository refreshTokenRepository;
    private UserEntityMapper userEntityMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AuthenticationManager authenticationManager;

    private UserProperties userProperties;

    @Autowired
    public UserService (UserRepository userRepository,
                        PhoneRepository phoneRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        UserEntityMapper userEntityMapper,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        AuthenticationManager authenticationManager,
                        UserProperties userProperties) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userEntityMapper = userEntityMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.userProperties = userProperties;
    }

    @Override
    public JwtResponse login(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        JwtResponse jwtResponse = TokenUtils.createToken(userDetails.getUserEntity().getName(), userDetails.getUserEntity().getEmail());

        List<RefreshTokenEntity> refreshTokenEntities = this.refreshTokenRepository.findByUserInfo(userDetails.getUserEntity());

        if (!refreshTokenEntities.isEmpty()) {
            this.refreshTokenRepository.deleteAll(refreshTokenEntities);
        }

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(jwtResponse.getRefreshToken())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .userInfo(userDetails.getUserEntity())
                .build();

        this.refreshTokenRepository.save(refreshTokenEntity);

        return jwtResponse;
    }

    @Override
    public DetailUserResponseDto findById(UUID userId) {
        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("No existe Usuario o no esta activo en el sistema", HttpStatus.NOT_FOUND));
        return this.userEntityMapper.mapperEntityToDetailsResponseDto(userEntity);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto userRequest) {

        this.userRepository.findOneByEmail(userRequest.getEmail()).map(x -> {
            throw new BusinessException("El correo ya esta registrado", HttpStatus.CONFLICT);
        });

        Util.validatePatter(userRequest.getEmail(), userProperties.getEmailRegex(), userProperties.getEmailMessage());
        Util.validatePatter(userRequest.getPassword(), userProperties.getPasswordRegex(), userProperties.getPasswordMessage());

        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

        UserEntity user = this.userEntityMapper.mapperUserRequestDtoToEntity(userRequest);

        UserEntity userEntity = this.userRepository.save(user);

        List<PhoneEntity> phoneEntities = userRequest.getPhones().stream()
                .map(phone -> this.userEntityMapper.mapperPhoneRequestDtoToSaveEntity(phone, userEntity))
                .collect(Collectors.toList());

        this.phoneRepository.saveAll(phoneEntities);

        System.out.println("data: " + userEntity.getName());

        JwtResponse jwtResponse = TokenUtils.createToken(userEntity.getName(), userEntity.getEmail());

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(jwtResponse.getRefreshToken())
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .userInfo(userEntity)
                .build();

        this.refreshTokenRepository.save(refreshTokenEntity);

        RegisterUserResponseDto registerUserResponseDto = this.userEntityMapper.mapperEntityToRegisterResponseDto(userEntity, jwtResponse);

        return registerUserResponseDto;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public DetailUserResponseDto modifyUser(ModifyUserRequestDto userRequest) {

        UserEntity user = this.userRepository.findByIdAndIsActiveTrue(userRequest.getId())
                .orElseThrow(() -> new BusinessException("No existe Usuario o no esta activo en el sistema", HttpStatus.NOT_FOUND));

        if (userRequest.getName() != null && !(userRequest.getName().trim().isEmpty())) {
            user.setName(userRequest.getName());
        }
        if (userRequest.getPassword() != null && !(userRequest.getPassword().trim().isEmpty())) {
            Util.validatePatter(userRequest.getPassword(), userProperties.getPasswordRegex(), userProperties.getPasswordMessage());
            user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        }

        user.setModified(Instant.now());

        UserEntity userEntity = this.userRepository.save(user);

        if (user.getPhones() != null && !(userRequest.getPhones().isEmpty())) {

            List<PhoneEntity> phoneList = userEntity.getPhones();

            List<PhoneEntity> phoneEntities = userRequest.getPhones().stream()
                    .filter(Objects::nonNull)
                    .map(phoneRequest -> {
                        switch (phoneRequest.getFlag()) {
                            case "add":
                                return this.userEntityMapper.mapperPhoneRequestDtoToSaveEntity(phoneRequest, userEntity);
                            case "modify":
                            case "delete":
                                    Optional<PhoneEntity> phoneToModifyOrDelete = phoneList.stream()
                                            .filter(p -> p.getId().equals(phoneRequest.getId()))
                                            .findFirst();
                                    if (phoneToModifyOrDelete.isPresent()) {
                                        return phoneRequest.getFlag().equals("modify")
                                                ? this.userEntityMapper.mapperPhoneRequestDtoToModifyEntity(phoneRequest, phoneToModifyOrDelete.get())
                                                : this.userEntityMapper.mapperPhoneRequestDtoToDeleteEntity(phoneToModifyOrDelete.get());
                                    } else {
                                        throw new BusinessException("Acción no válida al teléfono", HttpStatus.BAD_REQUEST);
                                    }
                            default:
                                throw new BusinessException("Bandera no válida: " + phoneRequest.getFlag(), HttpStatus.BAD_REQUEST);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            this.phoneRepository.saveAll(phoneEntities);
        }
        return this.userEntityMapper.mapperEntityToDetailsResponseDto(userEntity);
    }

    @Override
    public DeleteUseResponse deleteUser(UUID userId) {
        this.userRepository.deleteById(userId);
        return new DeleteUseResponse("Usuario eliminado");
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return this.refreshTokenRepository.findByToken(refreshTokenRequest.getToken())
                .map(token -> {
                    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
                        refreshTokenRepository.delete(token);
                        throw new BusinessException("El token de actualización expiró. Por favor, haz una nueva solicitud de inicio de sesión", HttpStatus.UNAUTHORIZED);
                    }
                    return token;
                })
                .map(RefreshTokenEntity::getUserInfo)
                .map(userEntity -> {
                    JwtResponse jwtResponse = TokenUtils.createToken(userEntity.getName(), userEntity.getEmail());

                    RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                            .token(jwtResponse.getRefreshToken())
                            .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                            .userInfo(userEntity)
                            .build();

                    this.refreshTokenRepository.save(refreshTokenEntity);
                    return jwtResponse;
                })
                .orElseThrow(() -> new BusinessException("El token de actualización no está en la base de datos", HttpStatus.NOT_FOUND));
    }

}
