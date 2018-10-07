package com.kite.playground.vkgallery.entity.vo;

import java.util.Collection;
import java.util.List;

import com.kite.playground.vkgallery.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupsResult {
    private Collection<Group> favourites;
    private Collection<Group> allGroups;
}
