package com.kite.playground.vkgallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;
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
        return albumManager.updateAlbum(album);
    }

    @PutMapping("album/{id}")
    public Boolean deleteAlbum(@PathVariable long id) {
        albumManager.deleteAlbum(id);
        return true;
    }

    @PutMapping("album/{albumId}/image")
    public Boolean addImage(@PathVariable long albumId, @RequestBody Image image) {
        albumManager.addImage(image, albumId);
        return true;
    }

    @GetMapping("album/{albumId}/images")
    public List<Image> getAlbumImages(@PathVariable long albumId) {
        return albumManager.loadImages(albumId);
    }
}
