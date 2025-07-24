package com.tuandat.antifraudwp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tuandat.antifraudwp.model.MyAppUser;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long>{
    Optional<MyAppUser> findByUsername(String username);
    Optional<MyAppUser> findByEmail(String email);
} 