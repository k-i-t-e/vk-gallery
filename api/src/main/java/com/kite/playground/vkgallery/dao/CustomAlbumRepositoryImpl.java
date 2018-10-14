package com.kite.playground.vkgallery.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;

@Repository
public class CustomAlbumRepositoryImpl extends NamedParameterJdbcDaoSupport implements CustomAlbumRepository {
    private static final String FIND_LATEST_IMAGE_FOR_ALBUMS_QUERY =
            "SELECT a.id as album_id, (" +
                    "SELECT i.*  FROM vk_gallery.album_image i " +
                    "WHERE i.album_id = a.id " +
                    "ORDER BY i.id " +
                    "DESC LIMIT 1 OFFSET 0" +
                ") FROM vk_gallery.album a";

    @Override
    public List<Album> findLatestImageForAlbums() {
        return getJdbcTemplate().query(FIND_LATEST_IMAGE_FOR_ALBUMS_QUERY, (rs, rowNum) -> {
            Album album = new Album();
            album.setId(rs.getLong(Columns.ALBUM_ID.name()));

            Image cover = new Image();
            cover.setId(rs.getLong(Columns.ID.name()));

            album.setCover(cover);
            return album;
        });
    }

    enum Columns {
        ALBUM_ID,
        ID
    }
}
