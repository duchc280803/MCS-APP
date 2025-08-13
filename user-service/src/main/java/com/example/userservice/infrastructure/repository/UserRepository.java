package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsernameAndIsDeleted(String loginId, Boolean isDeleted);

    @Query("select u from User u where u.email = :email and u.isDeleted = false")
    List<User> findByEmail(String email);

    Optional<User> findFirstByEmailAndIsDeleted(String email, Boolean isDeleted);

}
