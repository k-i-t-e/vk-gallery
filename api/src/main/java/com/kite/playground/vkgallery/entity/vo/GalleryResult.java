package com.kite.playground.vkgallery.entity.vo;

import java.util.List;

import com.kite.playground.vkgallery.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GalleryResult {
    private final List<Image> images;
    private final int nextPageOffset;
}
