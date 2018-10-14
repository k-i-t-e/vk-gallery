package com.kite.playground.vkgallery.dao;

import java.util.List;

import com.kite.playground.vkgallery.entity.Album;

public interface CustomAlbumRepository {
    List<Album> findLatestImageForAlbums();
}
