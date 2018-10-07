package com.kite.playground.vkgallery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "favourite_group", schema = "vk_gallery")
public class Group {
    @Id
    private Long id;
    private String domain;
    private String name;
    @Column(name = "group_alias")
    @EqualsAndHashCode.Exclude
    private String alias;
    @EqualsAndHashCode.Exclude
    private Long createdBy;
    private String imageUrl;
}
