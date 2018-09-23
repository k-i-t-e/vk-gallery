package com.kite.playground.vkgallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.vo.GalleryResult;
import com.kite.playground.vkgallery.manager.GalleryManager;

@RestController
public class GalleryController extends AbstractRestController {
    private GalleryManager galleryManager;

    @Autowired
    public GalleryController(GalleryManager galleryManager) {
        this.galleryManager = galleryManager;
    }

    @GetMapping("images/{groupId}")
    public GalleryResult getImages(@PathVariable String groupId,
                                   @RequestParam(required = false, defaultValue = "100") int pageSize,
                                   @RequestParam(required = false, defaultValue = "0") int offset) {
        return galleryManager.getImages(groupId, pageSize, offset);
    }
}
