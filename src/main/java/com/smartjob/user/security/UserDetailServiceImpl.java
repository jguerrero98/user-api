package com.smartjob.user.security;

import com.smartjob.user.entity.UserEntity;
import com.smartjob.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findOneByEmail(email);
        return user.map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}
