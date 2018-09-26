package com.kite.playground.vkgallery.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kite.playground.vkgallery.entity.Group;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    List<Group> findAllByCreatedBy(long createdBy);
}
