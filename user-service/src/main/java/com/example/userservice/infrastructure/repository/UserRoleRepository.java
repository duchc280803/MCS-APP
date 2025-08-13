package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    Optional<UserRole> findByName(String role);
}
