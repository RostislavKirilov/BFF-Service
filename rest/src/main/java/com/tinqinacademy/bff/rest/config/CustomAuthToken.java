package com.tinqinacademy.bff.rest.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthToken extends AbstractAuthenticationToken {
    private final CustomUser user;

    public CustomAuthToken(CustomUser user) {
        super(user.getAuthorities());
        this.user = user;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return user.getAuthorities();
    }

    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }
}
