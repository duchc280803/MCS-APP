package com.example.userservice.infrastructure.security.auth;

import com.example.userservice.domain.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDetail implements UserDetails {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private String userId;

    private String loginId;

    private String officeId;

    private String password;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetail fromEntity(User user) {
        return UserDetail.builder()
                .loginId(user.getUsername())
                .userId(user.getId())
                .password(user.getPassword())
                .role(user.getMember().getRole().getName())
                .build();
    }


}
