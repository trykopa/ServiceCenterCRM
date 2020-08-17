package com.googe.ssadm.sc.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
