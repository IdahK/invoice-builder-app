package org.invoicebuilder.users.domain;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.invoicebuilder.users.service.UserRoleService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {

    private final User user;
    private final UserRoleService userRoleService;

    public SecurityUser(User user, UserRoleService userRoleService) {
        this.user = user;
        this.userRoleService = userRoleService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roleNames = userRoleService.getUserRoleNames(user.getUserId());
        return roleNames.stream()
                .filter(Objects::nonNull)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getUserStatus() != UserStatus.DISABLED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getUserStatus() != UserStatus.DISABLED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getUserStatus() != UserStatus.DISABLED;
    }
}
