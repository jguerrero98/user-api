package com.smartjob.user.repository;

import com.smartjob.user.entity.RefreshTokenEntity;
import com.smartjob.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Integer> {
    Optional<RefreshTokenEntity> findByToken(String token);
    List<RefreshTokenEntity> findByUserInfo(UserEntity userEntity);
}
