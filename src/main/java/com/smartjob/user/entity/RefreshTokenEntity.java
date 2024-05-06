package com.smartjob.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    private Instant expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userInfo;

}
