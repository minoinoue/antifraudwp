package com.tuandat.antifraudwp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tuandat.antifraudwp.model.MyAppUser;
import com.tuandat.antifraudwp.repository.MyAppUserRepository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Service
@AllArgsConstructor 
public class MyAppUserService implements UserDetailsService{
    @Autowired
    private MyAppUserRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyAppUser> user = repository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            if (!userObj.isVerified()) {
                throw new AccountStatusException("Tài khoản chưa xác thực email.") {};
            }
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + userObj.getRole())))
                    .build();    
        }else{
            throw new AccountStatusException("Tài khoản không tồn tại.") {};
        }
    }
    
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
} 