package com.kite.playground.vkgallery.dao;

import org.springframework.data.repository.CrudRepository;

import com.kite.playground.vkgallery.entity.VkUser;

public interface VkUserRepository extends CrudRepository<VkUser, Long> {
}
