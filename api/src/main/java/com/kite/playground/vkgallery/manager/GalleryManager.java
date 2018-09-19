package com.kite.playground.vkgallery.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.manager.client.VkClient;

@Service
public class GalleryManager {
    private final VkClient vkClient;

    @Autowired
    public GalleryManager(VkClient vkClient) {
        this.vkClient = vkClient;
    }

    public List<Image> getImages(String groupId) {
        return vkClient.loadImages(groupId);
    }
}
