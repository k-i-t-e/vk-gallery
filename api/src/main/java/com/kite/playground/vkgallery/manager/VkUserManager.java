package com.kite.playground.vkgallery.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.dao.VkUserRepository;
import com.kite.playground.vkgallery.entity.VkUser;

@Service
public class VkUserManager {
    private VkUserRepository userRepository;

    @Autowired
    public VkUserManager(VkUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VkUser createUser(VkUser newUser) {
        return userRepository.findById(newUser.getId()).orElseGet(() -> {
            userRepository.save(newUser);
            return newUser;
        });
    }
}
