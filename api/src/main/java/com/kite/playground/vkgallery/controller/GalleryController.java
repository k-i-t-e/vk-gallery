package com.kite.playground.vkgallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.manager.GalleryManager;

@RestController
public class GalleryController extends AbstractRestController {
    private GalleryManager galleryManager;

    @Autowired
    public GalleryController(GalleryManager galleryManager) {
        this.galleryManager = galleryManager;
    }

    @GetMapping("images/{groupId}")
    public List<Image> getImages(@PathVariable String groupId) {
        return galleryManager.getImages(groupId);
    }
}
