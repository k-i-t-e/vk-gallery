package com.kite.playground.vkgallery.security;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.util.MultiValueMap;

public class VkAuthorizationCodeAccessTokenProvider extends AuthorizationCodeAccessTokenProvider {
    @Override
    protected String getAccessTokenUri(OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form) {
        String accessTokenUri = resource.getAccessTokenUri();

        logger.debug("Retrieving token from $accessTokenUri");

        String separator = "?";
        if (accessTokenUri.contains("?")) {
            separator = "&";
        }

        StringBuilder builder = new StringBuilder(accessTokenUri);
        for (String key : form.keySet()) {
            builder.append(separator);
            builder.append(key).append("={").append(key).append("}");
            separator = "&";
        }

        return builder.toString();
    }
}
