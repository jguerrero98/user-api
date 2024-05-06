package com.smartjob.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private String cityCode;

    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

    private Boolean isActive;

}
