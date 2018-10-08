package com.kite.playground.vkgallery.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "album", schema = "vk_gallery")
@Data
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "album_id_generator", sequenceName = "s_album", schema = "vk_gallery", allocationSize = 1)
    private Long id;
    private String name;
    private Long createdBy;
    private LocalDateTime createdAt;

    public Album(String name, Long createdBy, LocalDateTime createdAt) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
}
