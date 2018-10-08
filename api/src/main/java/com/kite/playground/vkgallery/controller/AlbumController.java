package com.kite.playground.vkgallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.manager.AlbumManager;

@RestController
public class AlbumController extends AbstractRestController {
    private AlbumManager albumManager;

    @Autowired
    public AlbumController(AlbumManager albumManager) {
        this.albumManager = albumManager;
    }

    @GetMapping("album")
    public List<Album> getAlbums() {
        return albumManager.loadAll();
    }

    @PostMapping("album")
    public Album createAlbum(String name) {
        return albumManager.create(name);
    }

    @PutMapping("album")
    public Album updateAlbum(Album album) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("album/{id}")
    public Boolean deleteAlbum(@PathVariable long id) {
        albumManager.deleteAlbum(id);
        return true;
    }

    @GetMapping("album/{id}")
    public Album loadAlbum(@PathVariable long id) {
        throw new UnsupportedOperationException();
    }
}
