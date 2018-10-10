package com.kite.playground.vkgallery.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.kite.playground.vkgallery.entity.Image;

public interface ImageRepository extends CrudRepository<Image, Long> {
    Optional<Image> findByIdAndCreatedBy(long id, long createdBy);
}