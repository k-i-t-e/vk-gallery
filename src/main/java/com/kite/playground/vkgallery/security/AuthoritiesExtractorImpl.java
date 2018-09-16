package com.kite.playground.vkgallery.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthoritiesExtractorImpl implements AuthoritiesExtractor {
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
}
