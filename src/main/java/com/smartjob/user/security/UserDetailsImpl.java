package com.smartjob.user.security;

import com.smartjob.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserEntity getUserEntity() {
        return user;
    }
    public UUID getUserId() {
        return user.getId();
    }
    public String getEmail() {
        return user.getEmail();
    }
    public String getName() {
        return user.getName();
    }
}
