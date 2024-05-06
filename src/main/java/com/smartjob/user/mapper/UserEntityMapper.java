package com.smartjob.user.mapper;

import com.smartjob.user.dto.*;
import com.smartjob.user.entity.PhoneEntity;
import com.smartjob.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserEntityMapper {

    public RegisterUserResponseDto mapperEntityToRegisterResponseDto(UserEntity userEntity, JwtResponse jwtResponse) {

        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setId(userEntity.getId());
        registerUserResponseDto.setCreated(userEntity.getCreated());
        registerUserResponseDto.setModified(userEntity.getModified());
        registerUserResponseDto.setLastLogin(userEntity.getLastLogin());
        registerUserResponseDto.setToken(jwtResponse.getAccessToken());
        registerUserResponseDto.setRefreshToken(jwtResponse.getRefreshToken());
        registerUserResponseDto.setIsActive(userEntity.getIsActive());

        return RegisterUserResponseDto.builder()
                .id(userEntity.getId())
                .created(userEntity.getCreated())
                .modified(userEntity.getModified())
                .lastLogin(userEntity.getLastLogin())
                .token(jwtResponse.getAccessToken())
                .refreshToken(jwtResponse.getRefreshToken())
                .isActive(userEntity.getIsActive())
                .build();
    }

    public DetailUserResponseDto mapperEntityToDetailsResponseDto(UserEntity userEntity) {

        List<DetailPhoneResponseDto> detailPhoneList = new ArrayList<>();
        userEntity.getPhones().stream().forEach(phone -> {
            DetailPhoneResponseDto detailPhoneResponseDto = DetailPhoneResponseDto.builder()
                    .number(phone.getNumber())
                    .countryCode(phone.getCountryCode())
                    .cityCode(phone.getCityCode())
                    .build();
            detailPhoneList.add(detailPhoneResponseDto);
        });

        System.out.println("data");

        return DetailUserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .created(userEntity.getCreated())
                .modified(userEntity.getModified())
                .lastLogin(userEntity.getLastLogin())
                .isActive(userEntity.getIsActive())
                .phones(detailPhoneList)
                .build();
    }
    public UserEntity mapperUserRequestDtoToEntity(RegisterUserRequestDto userRequest) {

        return UserEntity.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .isActive(true)
                .build();
    }

    public PhoneEntity mapperPhoneRequestDtoToSaveEntity(RegisterPhoneRequestDto phoneRequest, UserEntity userEntity) {

        return PhoneEntity.builder()
                .number(phoneRequest.getNumber())
                .cityCode(phoneRequest.getCityCode())
                .countryCode(phoneRequest.getCountryCode())
                .user(userEntity)
                .isActive(true)
                .build();
    }

    public PhoneEntity mapperPhoneRequestDtoToSaveEntity(ModifyPhoneRequestDto phoneRequest, UserEntity userEntity) {

        return PhoneEntity.builder()
                .number(phoneRequest.getNumber())
                .cityCode(phoneRequest.getCityCode())
                .countryCode(phoneRequest.getCountryCode())
                .user(userEntity)
                .isActive(true)
                .build();
    }

    public PhoneEntity mapperPhoneRequestDtoToModifyEntity(ModifyPhoneRequestDto phoneRequest, PhoneEntity phoneEntity) {

        if (phoneRequest.getNumber() != null && !(phoneRequest.getNumber().trim().isEmpty())) {
            phoneEntity.setNumber(phoneRequest.getNumber());
        }
        if (phoneRequest.getCityCode() != null && !(phoneRequest.getCityCode().trim().isEmpty())) {
            phoneEntity.setCityCode(phoneRequest.getCityCode());
        }
        if (phoneRequest.getCountryCode() != null && !(phoneRequest.getCountryCode().trim().isEmpty())) {
            phoneEntity.setCountryCode(phoneRequest.getCountryCode());
        }
        return phoneEntity;
    }

    public PhoneEntity mapperPhoneRequestDtoToDeleteEntity(PhoneEntity phoneEntity) {
        phoneEntity.setIsActive(false);
        return phoneEntity;
    }

}
