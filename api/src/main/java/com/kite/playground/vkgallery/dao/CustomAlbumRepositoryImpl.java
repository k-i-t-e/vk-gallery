package com.kite.playground.vkgallery.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomAlbumRepositoryImpl implements CustomAlbumRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_LATEST_IMAGE_FOR_ALBUMS_QUERY =
            "SELECT a.id as album_id, (" +
                    "SELECT i.id  FROM vk_gallery.album_image i " +
                    "WHERE i.album_id = a.id " +
                    "ORDER BY i.id " +
                    "DESC LIMIT 1 OFFSET 0" +
                ") as image_id FROM vk_gallery.album a WHERE a.cover_id IS NULL AND a.created_by = ?";

    @Override
    public Map<Long, Long> findLatestImageForAlbums(long createdBy) {
        Map<Long, Long> result = new HashMap<>();
        namedParameterJdbcTemplate.getJdbcTemplate().query(FIND_LATEST_IMAGE_FOR_ALBUMS_QUERY, rs -> {
                result.put(rs.getLong(1), rs.getLong(2));
            }, createdBy);

        return result;
    }
}
