package com.example.userservice.infrastructure.security.auth;

import com.example.userservice.domain.entity.user.Member;
import com.example.userservice.domain.entity.role.Permission;
import com.example.userservice.domain.entity.role.Role;
import com.example.userservice.infrastructure.repository.GroupPermissionRepository;
import com.example.userservice.infrastructure.repository.MemberRepository;
import com.example.userservice.infrastructure.repository.PermissionRepository;
import com.example.userservice.shared.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionAuthenticationFilter extends OncePerRequestFilter {


    private static final String AUTH_HEADER = "Authorization";

    private final GroupPermissionRepository groupPermissionRepository;
    private final PermissionRepository permissionRepository;
    private final MemberRepository memberRepository;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        final String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            List<String> whiteList = Constants.WHITE_LIST_API;
            if (whiteList.stream().anyMatch(path::contains)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (this.canAccessApi(request)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            logger.error(e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Method check user permission to access API
     *
     * @param request HttpServletRequest
     * @return True if user has permission to access API, otherwise False
     */
    private boolean canAccessApi(HttpServletRequest request) {
        var user = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findByUserIdAndIsDeleted(user.getUserId(), false).orElse(null);

        if (member == null) {
            return false;
        }

        if (member.getRole().getName().equals(Role.ROLE_ADMIN.getValue())) {
            return true;
        }

        // Get role permissions
        List<String> permissionIds = groupPermissionRepository.findByUserRoleId(member.getRole().getId())
                .stream().map(e -> e.getId().getPermissionId()).toList();
        // Find all permissions
        List<Permission> userPermissions = permissionRepository.findByIdInAndIsDeleted(permissionIds, false);
        // Extract request URI path
        List<String> requestParts = Arrays.stream(request.getRequestURI().replace(contextPath, "").split("/"))
                .filter(e -> !e.isBlank()).toList();
        if (requestParts.size() >= 3) {
            String mainRequestUrl = requestParts.get(0) + "/" + requestParts.get(1);
            String methodPath = requestParts.get(2);
            List<Permission> allowedPermissions = permissionRepository.findByScreenCodeAndIdInAndIsDeleted(
                    mainRequestUrl,
                    userPermissions.stream().map(Permission::getId).toList(),
                    false
            );
            // Check screen permissions
            if (!allowedPermissions.isEmpty()) {
                for (Permission permission : allowedPermissions) {
                    if (methodPath.contains("create") && permission.getCanCreate()) {
                        return true;
                    } else if (methodPath.contains("delete") && permission.getCanDelete()) {
                        return true;
                    } else if (methodPath.contains("get") && permission.getCanRead()) {
                        return true;
                    } else if (methodPath.contains("update") && permission.getCanUpdate()) {
                        return true;
                    } else if (methodPath.contains("import") && permission.getCanImport()) {
                        return true;
                    } else if (methodPath.contains("export") && permission.getCanExport()) {
                        return true;
                    } else if (methodPath.contains("search")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}