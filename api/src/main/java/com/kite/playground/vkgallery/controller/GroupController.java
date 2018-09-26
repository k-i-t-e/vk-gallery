package com.kite.playground.vkgallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kite.playground.vkgallery.entity.Group;
import com.kite.playground.vkgallery.entity.vo.GroupsResult;
import com.kite.playground.vkgallery.manager.GroupManager;

@RestController
public class GroupController extends AbstractRestController {
    private GroupManager groupManager;

    @Autowired
    public GroupController(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @PostMapping("group")
    public Group createGroup(@RequestBody String groupId) {
        return groupManager.addToFavourites(groupId);
    }

    @GetMapping("group")
    public GroupsResult listGroups() {
        return groupManager.loadGroups();
    }

    @PutMapping("group")
    public Group updateGroup(@RequestBody Group group) {
        return groupManager.updateGroup(group);
    }

    @DeleteMapping("group/{id}")
    public Boolean deleteGroup(@PathVariable long id) {
        groupManager.deleteGroup(id);
        return true;
    }
}
