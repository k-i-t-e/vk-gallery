package com.kite.playground.vkgallery.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.kite.playground.vkgallery.dao.VkUserRepository;
import com.kite.playground.vkgallery.entity.VkUser;

public class VkAuthenticationProvider implements AuthenticationProvider {
    private final VkUserRepository repository;

    public VkAuthenticationProvider(VkUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() instanceof VkUser) {
            VkUser user = (VkUser) authentication.getPrincipal();
            repository.findById(user.getId()).orElseGet(() -> {
                repository.save(user);
                return user;
            });
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(OAuth2Authentication.class);
    }
}
