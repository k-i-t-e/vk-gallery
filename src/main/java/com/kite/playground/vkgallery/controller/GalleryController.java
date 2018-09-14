package com.kite.playground.vkgallery.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.Image;
import reactor.core.publisher.Mono;

@RestController("images")
public class GalleryController {
    @GetMapping()
    public Mono<List<Image>> getImages() {
        return Mono.just(List.of(new Image("test", LocalDateTime.now())));
    }
}
