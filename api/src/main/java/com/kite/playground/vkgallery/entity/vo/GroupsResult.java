package com.kite.playground.vkgallery.entity.vo;

import java.util.List;

import com.kite.playground.vkgallery.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupsResult {
    private List<Group> favourites;
    private List<Group> allGroups;
}
