package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUserIdAndIsDeleted(String userId, Boolean isDeleted);

}
