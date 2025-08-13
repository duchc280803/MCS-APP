package com.example.userservice.domain.entity.role;

import com.example.userservice.domain.entity.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_role", schema = "dbo")

public class UserRole extends BaseEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tpp_group_permission",
            joinColumns = @JoinColumn(name = "user_role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @SQLRestriction("is_deleted = 0")
    private Set<Permission> permissions;

}

