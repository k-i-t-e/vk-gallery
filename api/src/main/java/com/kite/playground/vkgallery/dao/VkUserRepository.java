package com.kite.playground.vkgallery.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.entity.VkUser;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface VkUserRepository extends CrudRepository<VkUser, Long> {
}
