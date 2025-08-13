package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.user.TokenExpired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface TokenExpiredRepository extends JpaRepository<TokenExpired, Long> {

        List<TokenExpired> findAllByAccessToken(String token);

    }
