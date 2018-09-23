package com.kite.playground.vkgallery.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kite.playground.vkgallery.entity.vo.GalleryResult;
import com.kite.playground.vkgallery.manager.client.VkClient;

@Service
public class GalleryManager {
    private final VkClient vkClient;

    @Autowired
    public GalleryManager(VkClient vkClient) {
        this.vkClient = vkClient;
    }

    public GalleryResult getImages(String groupId, int pageSize, int offset) {
        return vkClient.loadImages(groupId, pageSize, offset);
    }
}
