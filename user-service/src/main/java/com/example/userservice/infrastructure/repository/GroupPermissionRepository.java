package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.role.GroupPermission;
import com.example.userservice.domain.entity.role.GroupPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupPermissionRepository extends JpaRepository<GroupPermission, GroupPermissionId> {

    @Query("select t from GroupPermission t where t.id.userRoleId = ?1")
    List<GroupPermission> findByUserRoleId(String userRole);

}
