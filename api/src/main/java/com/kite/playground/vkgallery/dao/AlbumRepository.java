package com.kite.playground.vkgallery.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kite.playground.vkgallery.entity.Album;

public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findAllByCreatedBy(long owner);
    Optional<Album> findByIdAndCreatedBy(long id, long createdBy);
    Optional<Album> findByNameAndCreatedBy(String name, long createdBy);
}
