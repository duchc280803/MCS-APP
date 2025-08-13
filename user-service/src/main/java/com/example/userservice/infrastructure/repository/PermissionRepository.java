package com.example.userservice.infrastructure.repository;

import com.example.userservice.domain.entity.role.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    List<Permission> findByIdInAndIsDeleted(List<String> ids, boolean isDeleted);

    @Query("Select c from Permission c where c.screen.code like %:screenCode% and c.id in :ids and c.screen.isDeleted = :isDeleted")
    List<Permission> findByScreenCodeAndIdInAndIsDeleted(String screenCode, List<String> ids, boolean isDeleted);

}
