package com.example.userservice.domain.entity.user;

import com.example.userservice.domain.entity.role.UserRole;
import com.example.userservice.domain.entity.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private UserRole role;

}

