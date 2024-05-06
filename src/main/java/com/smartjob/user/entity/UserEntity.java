package com.smartjob.user.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Where(clause = "is_active = true")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<PhoneEntity> phones;

    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(name = "modified", nullable = false)
    private Instant modified;

    @CreatedDate
    @Column(nullable = false)
    private Instant lastLogin;

    @Column(nullable = false)
    private Boolean isActive;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", lastLogin=" + lastLogin +
                ", isActive=" + isActive +
                '}';
    }
}
