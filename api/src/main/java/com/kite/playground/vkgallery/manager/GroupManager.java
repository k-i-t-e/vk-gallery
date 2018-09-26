package com.kite.playground.vkgallery.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.dao.GroupRepository;
import com.kite.playground.vkgallery.entity.Group;
import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.entity.vo.GroupsResult;
import com.kite.playground.vkgallery.manager.client.VkClient;

@Service
public class GroupManager {
    private VkClient vkClient;
    private GroupRepository groupRepository;
    private AuthManager authManager;

    @Autowired
    public GroupManager(VkClient vkClient, GroupRepository groupRepository,
                        AuthManager authManager) {
        this.vkClient = vkClient;
        this.groupRepository = groupRepository;
        this.authManager = authManager;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Group addToFavourites(String groupId) {
        Group group = vkClient.getGroup(groupId);
        group.setCreatedBy(authManager.getCurrentUser().getId());
        return groupRepository.save(group);
    }

    public GroupsResult loadGroups() {
        VkUser currentUser = authManager.getCurrentUser();
        List<Group> favouriteGroups = groupRepository.findAllByCreatedBy(currentUser.getId());
        List<Group> allGroups = vkClient.getUsersGroups();
        return new GroupsResult(favouriteGroups, allGroups);
    }
}
