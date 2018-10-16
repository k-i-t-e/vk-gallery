package com.kite.playground.vkgallery.dao;

import java.util.Map;

public interface CustomAlbumRepository {
    Map<Long, Long> findLatestImageForAlbums(long createdBy);
}
