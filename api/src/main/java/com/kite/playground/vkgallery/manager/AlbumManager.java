package com.kite.playground.vkgallery.manager;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.dao.AlbumRepository;
import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.VkUser;

@Service
public class AlbumManager {
    private AuthManager authManager;
    private AlbumRepository albumRepository;

    @Autowired
    public AlbumManager(AuthManager authManager, AlbumRepository albumRepository) {
        this.authManager = authManager;
        this.albumRepository = albumRepository;
    }

    public List<Album> loadAll() {
        VkUser currentUser = authManager.getCurrentUser();
        return albumRepository.findAllByCreatedBy(currentUser.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Album create(String name) {
        VkUser currentUser = authManager.getCurrentUser();
        return albumRepository.save(new Album(name, currentUser.getId(), LocalDateTime.now(Clock.systemUTC())));
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
}
