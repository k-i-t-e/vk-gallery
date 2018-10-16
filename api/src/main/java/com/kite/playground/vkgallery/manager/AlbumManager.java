package com.kite.playground.vkgallery.manager;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.kite.playground.vkgallery.dao.AlbumRepository;
import com.kite.playground.vkgallery.dao.ImageRepository;
import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.entity.VkUser;

@Service
public class AlbumManager {
    private AuthManager authManager;
    private AlbumRepository albumRepository;
    private ImageRepository imageRepository;

    @Autowired
    public AlbumManager(AuthManager authManager, AlbumRepository albumRepository, ImageRepository imageRepository) {
        this.authManager = authManager;
        this.albumRepository = albumRepository;
        this.imageRepository = imageRepository;
    }

    public Collection<Album> loadAll() {
        VkUser currentUser = authManager.getCurrentUser();
        Map<Long, Album> albumMap = albumRepository.findAllByCreatedBy(currentUser.getId()).stream()
            .collect(Collectors.toMap(Album::getId, a -> a));

        if (albumMap.values().stream().anyMatch(a -> a.getCover() == null)) {
            Map<Long, Long> lastImagesMap = albumRepository.findLatestImageForAlbums(currentUser.getId());
            for (Image image : imageRepository.findAllById(lastImagesMap.values())) {
                Album album = albumMap.get(image.getAlbumId());
                if (album != null) {
                    album.setCover(image);
                }
            }
        }

        return albumMap.values();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Album create(String name) {
        VkUser currentUser = authManager.getCurrentUser();

        albumRepository.findByNameAndCreatedBy(name, currentUser.getId()).ifPresent((album) -> {
            throw new IllegalArgumentException("Album already exists");
        });

        return albumRepository.save(new Album(name, currentUser.getId(), LocalDateTime.now(Clock.systemUTC())));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Album updateAlbum(Album album) {
        VkUser currentUser = authManager.getCurrentUser();
        Album existingAlbum = albumRepository.findByIdAndCreatedBy(album.getId(), currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No such album for user " + currentUser.getFirstName() + " " + currentUser.getLastName()));

        existingAlbum.setName(album.getName());
        existingAlbum.setCover(album.getCover());
        albumRepository.save(existingAlbum);
        return existingAlbum;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAlbum(long id) {
        Album album = load(id);
        albumRepository.delete(album);
    }

    public Album load(long id) {
        VkUser currentUser = authManager.getCurrentUser();
        return albumRepository.findByIdAndCreatedBy(id, currentUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException("No album with ID %s found for current user"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addImage(Image image, long albumId) {
        VkUser currentUser = authManager.getCurrentUser();

        if (image.getId() == null) {
            Assert.isTrue(!CollectionUtils.isEmpty(image.getUrls()), "URLs cannot be empty");
            Assert.isTrue(image.getUrls().containsKey(Image.THUMBNAIL_RESOLUTION), "Thumbnail must be present");

            image.setThumbnail(image.getUrls().get(Image.THUMBNAIL_RESOLUTION));
            image.setCreatedDate(LocalDateTime.now(Clock.systemUTC()));

            imageRepository.findByAlbumIdAndThumbnail(albumId, image.getThumbnail())
                    .ifPresent(i -> {
                        throw new IllegalArgumentException("Such image is already present in album " + albumId);
                    });
        } else {
            imageRepository.findByIdAndCreatedBy(image.getId(), currentUser.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Image with ID %d not found for current user", image.getId())));
        }

        image.setCreatedBy(currentUser.getId());
        image.setAlbumId(albumId);

        imageRepository.save(image);
    }

    public List<Image> loadImages(long albumId) {
        return imageRepository.findAllByAlbumId(albumId);
    }
}
