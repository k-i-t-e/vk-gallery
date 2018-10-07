package com.kite.playground.vkgallery.dao;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.kite.playground.vkgallery.entity.Group;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    Set<Group> findAllByCreatedBy(long createdBy);
    Optional<Group> findByIdAndCreatedBy(long id, long ownerId);
}
