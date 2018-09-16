package com.kite.playground.vkgallery.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.Image;

@RestController
@RequestMapping("images")
public class GalleryController {
    @GetMapping()
    public List<Image> getImages() {
        return Collections.singletonList(new Image("test", LocalDateTime.now()));
    }
}
