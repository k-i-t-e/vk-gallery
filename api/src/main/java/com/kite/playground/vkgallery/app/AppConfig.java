package com.kite.playground.vkgallery.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;

@Configuration
public class AppConfig {
    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        return new VkApiClient(transportClient);
    }
}
