package com.kite.playground.vkgallery.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kite.playground.vkgallery.entity.VkUser;

@Repository
public interface UserRepository extends CrudRepository<VkUser, Long> {
}
