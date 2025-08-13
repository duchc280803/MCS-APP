package com.example.userservice.domain.entity.role;

import com.example.userservice.domain.entity.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tpp_group_permission", schema = "dbo")
public class GroupPermission extends BaseEntity {

    @EmbeddedId
    private GroupPermissionId id;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @MapsId("userRoleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id", nullable = false)
    private UserRole userRole;

}