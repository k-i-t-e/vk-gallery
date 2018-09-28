package com.kite.playground.vkgallery.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "favourite_group", schema = "vk_gallery")
public class Group {
    @Id
    private Long id;
    private String domain;
    private String name;
    @Column(name = "group_alias")
    private String alias;
    private Long createdBy;
    private transient String imageUrl;
}
