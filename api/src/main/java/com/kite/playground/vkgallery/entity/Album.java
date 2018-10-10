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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "album_id_generator")
    @SequenceGenerator(name = "album_id_generator", sequenceName = "s_album", schema = "vk_gallery", allocationSize = 1)
    private Long id;
    private String name;
    private Long createdBy;
    private LocalDateTime createdDate;

    public Album(String name, Long createdBy, LocalDateTime createdDate) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }
}
