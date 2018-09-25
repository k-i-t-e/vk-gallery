package com.kite.playground.vkgallery.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Group {
    @Id
    private Integer id;

    private String domain;

    private String name;
}
