package com.example.userservice.domain.entity.user;

import com.example.userservice.domain.entity.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Basic
    @Column(name = "full_name", nullable = false, length = 60)
    private String fullName;

    @Basic
    @Column(name = "email", length = 254)
    private String email;

    @Basic
    @Column(name = "tel_no", length = 18)
    private String telNo;

    @Basic
    @Column(name = "password", length = 64)
    private String password;

    @Basic
    @Column(name = "username", length = 64)
    private String username;

    @Basic
    @Column(name = "profile_image")
    private String profileImage;

    @Basic
    @Column(name = "note", length = 250)
    private String note;

    @Basic
    @Column(name = "expired_at")
    private OffsetDateTime passwordExpiredAt;

    @OneToOne(mappedBy = "user")
    private Member member;

}
