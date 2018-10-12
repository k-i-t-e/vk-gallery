package com.kite.playground.vkgallery.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.kite.playground.vkgallery.entity.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {
    Optional<Image> findByIdAndCreatedBy(long id, long createdBy);
    Optional<Image> findByAlbumIdAndThumbnail(long albumId, String thumbnail);
    List<Image> findAllByAlbumId(long albumId);
}