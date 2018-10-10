package com.kite.playground.vkgallery.dao;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.entity.VkUser;

public class ImageRepositoryTest extends AbstractDaoTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private VkUserRepository vkUserRepository;

    private Album album;
    private VkUser vkUser;

    @Before
    public void setUp() {
        vkUser = new VkUser(1L, "test", "test", Collections.emptyMap());
        vkUserRepository.save(vkUser);
        album = new Album("testAlbum", vkUser.getId(), LocalDateTime.now(Clock.systemUTC()));
        albumRepository.save(album);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testCreateLoad() {
        Map<String, String> urls = new HashMap<>();
        urls.put(Image.THUMBNAIL_RESOLUTION, "test");
        Image image = new Image(1L, urls, null, "testThumbnail", album.getId(), vkUser.getId(),
                                LocalDateTime.now(Clock.systemUTC()));

        imageRepository.save(image);

        Assert.assertNotNull(image.getId());

        testEntityManager.flush();

        Optional<Image> loaded = imageRepository.findById(image.getId());

        Assert.assertTrue(loaded.isPresent());
        loaded.ifPresent(i -> {
            Assert.assertEquals(image.getUrls(), i.getUrls());
            Assert.assertEquals(image.getPostId(), i.getPostId());
        });
    }
}