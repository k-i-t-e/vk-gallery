package com.kite.playground.vkgallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.manager.AuthManager;

@RestController
public class UserController extends AbstractRestController {
    private AuthManager authManager;

    @Autowired
    public UserController(AuthManager authManager) {
        this.authManager = authManager;
    }

    @GetMapping("/user/current")
    public VkUser getUser() {
        return authManager.getCurrentUser();
    }

    @GetMapping("/user/token")
    public String getToken() {
        return authManager.getAccessToken();
    }
}
