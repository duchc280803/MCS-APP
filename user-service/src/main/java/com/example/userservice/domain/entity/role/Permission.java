package com.example.userservice.domain.entity.role;

import com.example.userservice.domain.entity.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "permission", schema = "dbo")
public class Permission extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "can_create")
    private Boolean canCreate;

    @Column(name = "can_delete")
    private Boolean canDelete;

    @Column(name = "can_read")
    private Boolean canRead;

    @Column(name = "can_update")
    private Boolean canUpdate;

    @Column(name = "can_import")
    private Boolean canImport;

    @Column(name = "can_export")
    private Boolean canExport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToMany(mappedBy = "permissions")
    private Set<UserRole> roles;

}