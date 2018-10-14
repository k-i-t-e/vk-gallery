package com.kite.playground.vkgallery.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.util.Utils;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AlbumRepositoryTest extends AbstractDaoTest {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private VkUserRepository vkUserRepository;

    private VkUser vkUser;

    @Before
    public void setUp() {
        vkUser = new VkUser(1L, "test", "test", Collections.emptyMap());
        vkUserRepository.save(vkUser);
    }

    @Test
    public void testCreateLoad() {
        Album album = new Album("test", vkUser.getId(), Utils.nowUTC());
        albumRepository.save(album);
        testEntityManager.flush();

        Assert.assertNotNull(album.getId());

        Optional<Album> albumOpt = albumRepository.findById(album.getId());
        Assert.assertTrue(albumOpt.isPresent());
        assertEquals(album, albumOpt.get());

        albumOpt = albumRepository.findByNameAndCreatedBy(album.getName(), vkUser.getId());
        Assert.assertTrue(albumOpt.isPresent());
        assertEquals(album, albumOpt.get());

        List<Album> loadedAlbums = albumRepository.findAllByCreatedBy(vkUser.getId());
        Assert.assertFalse(loadedAlbums.isEmpty());
        assertEquals(album, loadedAlbums.get(0));
    }

    @Test
    public void testLoadCover() {
        Album album = new Album("test", vkUser.getId(), Utils.nowUTC());
        albumRepository.save(album);

        Image cover = new Image(0L, Collections.emptyMap(), null, "thumbnail", album.getId(),
                vkUser.getId(), Utils.nowUTC());
        imageRepository.save(cover);

        album.setCover(cover);
        albumRepository.save(album);

        testEntityManager.flush();

        Album loadedAlbum = albumRepository.findById(album.getId()).get();
        ImageRepositoryTest.assertEquals(cover, loadedAlbum.getCover());

        List<Album> albums = albumRepository.findLatestImageForAlbums();
        Assert.assertFalse(albums.isEmpty());
    }

    private void assertEquals(Album album, Album loadedAlbum) {
        Assert.assertEquals(album.getId(), loadedAlbum.getId());
        Assert.assertEquals(album.getName(), loadedAlbum.getName());
        Assert.assertEquals(album.getCover(), loadedAlbum.getCover());
        Assert.assertEquals(album.getCreatedBy(), loadedAlbum.getCreatedBy());
        Assert.assertEquals(album.getCreatedDate(), loadedAlbum.getCreatedDate());
    }
}