package com.kite.playground.vkgallery.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "album_image", schema = "vk_gallery")
public class Image {
    public static final String THUMBNAIL_RESOLUTION = "604";

    private Long postId;

    @Convert(converter = UrlsConverter.class)
    private Map<String, String> urls;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_generator")
    @SequenceGenerator(name = "image_id_generator", sequenceName = "s_album_image", schema = "vk_gallery", allocationSize = 1)
    private Long id;
    private String thumbnail;
    private Long albumId;
    private Long createdBy;
    private LocalDateTime createdDate;

    public Image(Long postId, Map<String, String> urls) {
        this.postId = postId;
        this.urls = urls;
    }

    public static class UrlsConverter implements AttributeConverter<Map<String, String>, String> {

        @Override
        public String convertToDatabaseColumn(Map<String, String> attribute) {
            return attribute.entrySet().stream()
                .map(e -> e.getKey() + ':' + e.getValue())
                .collect(Collectors.joining(";"));
        }

        @Override
        public Map<String, String> convertToEntityAttribute(String dbData) {
            if (StringUtils.isBlank(dbData)) {
                return new HashMap<>();
            }

            return Arrays.stream(dbData.split(";"))
                .map(p -> {
                    String[] parts = p.split(":");
                    Assert.isTrue(parts.length == 2, "Unexpected URLs database value found: " + dbData);
                    return parts;
                })
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
        }
    }
}
