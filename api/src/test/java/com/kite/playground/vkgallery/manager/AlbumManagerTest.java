package com.kite.playground.vkgallery.manager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.kite.playground.vkgallery.dao.AlbumRepository;
import com.kite.playground.vkgallery.dao.ImageRepository;
import com.kite.playground.vkgallery.entity.Album;
import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.util.Utils;

public class AlbumManagerTest {
    private AlbumManager albumManager;

    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private AuthManager authManager;
    @Captor
    private ArgumentCaptor<Image> imageArgumentCaptor;

    private VkUser mockUser = new VkUser(1L, "test", "test", Collections.emptyMap());

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        albumManager = new AlbumManager(authManager, albumRepository, imageRepository);
        when(authManager.getCurrentUser()).thenReturn(mockUser);
    }

    @Test
    public void testLoadAllHaveCovers() {
        Image mockCover = new Image();
        Album mockAlbum = new Album(1L, "test", mockUser.getId(), Utils.nowUTC(), mockCover);
        when(albumRepository.findAllByCreatedBy(mockUser.getId())).thenReturn(Collections.singletonList(mockAlbum));

        Collection<Album> loadedAlbums = albumManager.loadAll();
        Assert.assertEquals(1, loadedAlbums.size());
        Assert.assertEquals(mockAlbum, loadedAlbums.iterator().next());

        verify(albumRepository, Mockito.never()).findLatestImageForAlbums(Mockito.anyLong());
        verify(imageRepository, Mockito.never()).findAllById(Mockito.anyCollection());
    }

    @Test
    public void testLoadWithoutCovers() {
        Image mockCover1 = new Image();
        Album mockAlbum1 = new Album(1L, "test", mockUser.getId(), Utils.nowUTC(), mockCover1);
        Album mockAlbum2 = new Album(2L, "test", mockUser.getId(), Utils.nowUTC(), null);
        Image mockCover2 = new Image();

        mockCover2.setId(2L);
        mockCover2.setAlbumId(mockAlbum2.getId());

        when(albumRepository.findAllByCreatedBy(mockUser.getId())).thenReturn(Arrays.asList(mockAlbum1, mockAlbum2));

        Map<Long, Long> resultMap = Collections.singletonMap(2L, 2L);
        when(albumRepository.findLatestImageForAlbums(mockUser.getId())).thenReturn(resultMap);

        when(imageRepository.findAllById(resultMap.values())).thenReturn(Collections.singletonList(mockCover2));

        Collection<Album> loadedAlbums = albumManager.loadAll();
        Assert.assertEquals(2, loadedAlbums.size());

        Iterator<Album> iterator = loadedAlbums.iterator();
        Assert.assertEquals(mockAlbum1, iterator.next());
        Assert.assertEquals(mockCover2, iterator.next().getCover());

        verify(albumRepository).findLatestImageForAlbums(mockUser.getId());
        verify(imageRepository).findAllById(resultMap.keySet());
    }

    @Test
    public void testAddNewImage() {
        Image imageToAdd = new Image(1L, Collections.singletonMap(Image.THUMBNAIL_RESOLUTION, "test"));
        long targetAlbumId = 1L;
        when(imageRepository.findByAlbumIdAndThumbnail(targetAlbumId, "test")).thenReturn(Optional.empty());

        albumManager.addImage(imageToAdd, targetAlbumId);

        verify(imageRepository).save(imageArgumentCaptor.capture());

        Image addedImage = imageArgumentCaptor.getValue();
        Assert.assertEquals(imageToAdd.getPostId(), addedImage.getPostId());
        Assert.assertEquals(imageToAdd.getUrls(), addedImage.getUrls());
        Assert.assertEquals("test", addedImage.getThumbnail());
        Assert.assertEquals(targetAlbumId, addedImage.getAlbumId().longValue());
        Assert.assertEquals(mockUser.getId(), addedImage.getCreatedBy());
        Assert.assertNotNull(addedImage.getCreatedDate());
    }
}