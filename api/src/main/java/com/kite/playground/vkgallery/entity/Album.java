package com.kite.playground.vkgallery.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "album", schema = "vk_gallery")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "album_id_generator", sequenceName = "s_album", schema = "vk_gallery", allocationSize = 1)
    private Long id;
    private String name;
    private Long createdBy;
    private LocalDateTime createdAt;
}
