package com.kite.playground.vkgallery.security;

import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import com.kite.playground.vkgallery.entity.VkUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VkPrincipalExtractor implements PrincipalExtractor {
    private static final String AUTH_SERVICE = "VK";

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        log.debug("credential map: {}", map);
        map.put("_authServiceType", AUTH_SERVICE);

        return new VkUser(map);
    }
}
