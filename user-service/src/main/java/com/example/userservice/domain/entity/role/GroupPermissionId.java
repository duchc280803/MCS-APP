package com.example.userservice.domain.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GroupPermissionId implements Serializable {
    private static final long serialVersionUID = -3238893579052065687L;
    @Column(name = "permission_id", nullable = false)
    private String permissionId;

    @Column(name = "user_role_id", nullable = false)
    private String userRoleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupPermissionId entity = (GroupPermissionId) o;
        return Objects.equals(this.permissionId, entity.permissionId) &&
                Objects.equals(this.userRoleId, entity.userRoleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionId, userRoleId);
    }

}
