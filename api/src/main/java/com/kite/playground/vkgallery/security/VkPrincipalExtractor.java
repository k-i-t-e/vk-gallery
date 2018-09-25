package com.kite.playground.vkgallery.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.manager.VkUserManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VkPrincipalExtractor implements PrincipalExtractor {
    private static final String AUTH_SERVICE = "VK";
    private static final String RESPONSE_KEY = "response";

    private VkUserManager vkUserManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    public VkPrincipalExtractor(VkUserManager userManager) {
        this.vkUserManager = userManager;
    }

    @Override
    public VkUser extractPrincipal(Map<String, Object> map) {
        log.debug("credential map: {}", map);
        map.put("_authServiceType", AUTH_SERVICE);

        if (!map.containsKey(RESPONSE_KEY) || !(map.get(RESPONSE_KEY) instanceof List)) {
            return null;
        }

        VkUser user = objectMapper.convertValue(((List) map.get(RESPONSE_KEY)).get(0), VkUser.class);

        vkUserManager.createUser(user);

        return user;
    }
}
