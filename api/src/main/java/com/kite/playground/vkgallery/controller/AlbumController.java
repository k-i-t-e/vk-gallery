package com.kite.playground.vkgallery.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.Album;

@RestController
public class AlbumController extends AbstractRestController {
    @GetMapping("album")
    public List<Album> getAlbums() {
        throw new UnsupportedOperationException();
    }

    @PostMapping("album")
    public Album createAlbum(String name) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("album")
    public Album updateAlbum(Album album) {
        throw new UnsupportedOperationException();
    }
}
