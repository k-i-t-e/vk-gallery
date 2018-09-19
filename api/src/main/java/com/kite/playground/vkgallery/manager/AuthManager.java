package com.kite.playground.vkgallery.manager;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

import com.kite.playground.vkgallery.entity.VkUser;

@Service
public class AuthManager {
    public VkUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof VkUser) {
                return (VkUser) principal;
            }
        }

        return null;
    }

    public String getAccessToken() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null && context.getAuthentication() instanceof OAuth2Authentication) {
            return ((OAuth2AuthenticationDetails) context.getAuthentication().getDetails()).getTokenValue();
        }

        return null;
    }
}
