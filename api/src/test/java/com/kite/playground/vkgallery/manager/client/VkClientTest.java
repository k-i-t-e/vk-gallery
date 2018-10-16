package com.kite.playground.vkgallery.manager.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.entity.vo.GalleryResult;
import com.kite.playground.vkgallery.manager.AuthManager;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetQuery;

public class VkClientTest {
    private static final String TEST_TOKEN = "TEST_TOKEN";
    private static final long TEST_REFRESH_PERIOD = 1000L;
    private static final int TEST_MAX_ACTIONS_PER_PERIOD = 5;

    private static final String TEST_PHOTO_75 = "TEST_PHOTO_75";
    private static final String TEST_PHOTO_130 = "TEST_PHOTO_130";
    private static final String TEST_PHOTO_604 = "TEST_PHOTO_604";
    private static final String TEST_PHOTO_807 = "TEST_PHOTO_807";
    private static final String TEST_PHOTO_1280 = "TEST_PHOTO_1280";
    private static final String TEST_PHOTO_2560 = "TEST_PHOTO_2560";

    @Mock
    private AuthManager authManager;
    @Mock
    private VkApiClient vkApiClient;
    @Mock
    private Wall wallMock;
    @Mock
    private WallGetQuery mockQuery;

    private LongAdder longAdder = new LongAdder();

    private VkClient vkClient;
    private VkUser testUser = new VkUser(1L, "test", "test", Collections.emptyMap());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        vkClient = new VkClient(authManager, vkApiClient, TEST_REFRESH_PERIOD, TEST_MAX_ACTIONS_PER_PERIOD);
        when(authManager.getCurrentUser()).thenReturn(testUser);
        when(authManager.getAccessToken()).thenReturn(TEST_TOKEN);
    }

    @Test
    public void testOnlyNActionsPerPeriod() throws InterruptedException, ClientException, ApiException {
        Wall wallMock = createWallMock(Collections.emptyList());

        when(vkApiClient.wall()).thenReturn(wallMock);

        ForkJoinPool pool = new ForkJoinPool(10);
        IntStream.range(0, 10).forEach(i -> pool.submit(() -> vkClient.loadImages("test", 0 ,0)));

        Thread.sleep(TEST_REFRESH_PERIOD / 2);
        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD, longAdder.intValue());

        Thread.sleep(TEST_REFRESH_PERIOD);

        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD * 2, longAdder.intValue());
    }

    @Test
    public void testParsePosts() throws ClientException, ApiException {
        List<WallPostFull> posts = IntStream.range(0, 10)
            .mapToObj(i -> mockPostWithAttachments())
            .collect(Collectors.toList());
        Wall wallMock = createWallMock(posts);

        when(vkApiClient.wall()).thenReturn(wallMock);

        GalleryResult result = vkClient.loadImages("test", 10, 0);

        Assert.assertEquals(10, result.getImages().size());
        Assert.assertEquals(10, result.getNextPageOffset());

        Map<String, String> urls = result.getImages().get(0).getUrls();
        Assert.assertEquals(TEST_PHOTO_75, urls.get("75"));
        Assert.assertEquals(TEST_PHOTO_130, urls.get("130"));
        Assert.assertEquals(TEST_PHOTO_604, urls.get("604"));
        Assert.assertEquals(TEST_PHOTO_807, urls.get("807"));
        Assert.assertEquals(TEST_PHOTO_1280, urls.get("1280"));
        Assert.assertEquals(TEST_PHOTO_2560, urls.get("2560"));
        Assert.assertEquals(posts.get(0).getId().intValue(), result.getImages().get(0).getPostId().intValue());

        verify(vkApiClient).wall();
    }

    @Test
    public void testGetPostsLessThanEnough() throws ClientException, ApiException {
        List<WallPostFull> posts = IntStream.range(0, 10) // return 10 posts at a time
            .mapToObj(i -> mockPostWithAttachments())
            .collect(Collectors.toList());
        Wall wallMock = createWallMock(posts);

        when(vkApiClient.wall()).thenReturn(wallMock);

        GalleryResult result = vkClient.loadImages("test", 30, 5);

        Assert.assertEquals(30, result.getImages().size());
        Assert.assertEquals(35, result.getNextPageOffset());

        verify(vkApiClient, times(3)).wall();
        verify(mockQuery).offset(5);
        verify(mockQuery).offset(15);
        verify(mockQuery).offset(25);
    }

    @Test
    public void testGetPostsMoreThanEnough() throws ClientException, ApiException {
        List<WallPostFull> posts = IntStream.range(0, 10) // return 10 posts at a time
            .mapToObj(i -> mockPostWithAttachments())
            .collect(Collectors.toList());
        Wall wallMock = createWallMock(posts);

        when(vkApiClient.wall()).thenReturn(wallMock);

        GalleryResult result = vkClient.loadImages("test", 15, 0);

        Assert.assertEquals(15, result.getImages().size());
        Assert.assertEquals(15, result.getNextPageOffset());

        verify(vkApiClient, times(2)).wall();
        verify(mockQuery).offset(0);
        verify(mockQuery).offset(10);
    }

    private Wall createWallMock(List<WallPostFull> posts) throws ApiException, ClientException {
        GetResponse mockResponse = mock(GetResponse.class);

        when(wallMock.get(Mockito.any(UserActor.class))).thenReturn(mockQuery);
        when(mockQuery.domain(any())).thenReturn(mockQuery);
        when(mockQuery.count(any())).thenReturn(mockQuery);
        when(mockQuery.offset(any())).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(mockResponse);
        when(mockResponse.getItems()).then(invocation -> {
            longAdder.increment();
            return posts;
        });

        return wallMock;
    }

    private WallPostFull mockPostWithAttachments() {
        Photo mockPhoto = mock(Photo.class);
        when(mockPhoto.getPhoto75()).thenReturn(TEST_PHOTO_75);
        when(mockPhoto.getPhoto130()).thenReturn(TEST_PHOTO_130);
        when(mockPhoto.getPhoto604()).thenReturn(TEST_PHOTO_604);
        when(mockPhoto.getPhoto807()).thenReturn(TEST_PHOTO_807);
        when(mockPhoto.getPhoto1280()).thenReturn(TEST_PHOTO_1280);
        when(mockPhoto.getPhoto2560()).thenReturn(TEST_PHOTO_2560);

        WallpostAttachment attachment1 = mock(WallpostAttachment.class);
        when(attachment1.getType()).thenReturn(WallpostAttachmentType.PHOTO);
        when(attachment1.getPhoto()).thenReturn(mockPhoto);

        WallpostAttachment attachment2 = mock(WallpostAttachment.class);
        when(attachment2.getType()).thenReturn(WallpostAttachmentType.AUDIO);

        WallPostFull post = mock(WallPostFull.class);
        when(post.getId()).thenReturn(new Random().nextInt());
        when(post.getAttachments()).thenReturn(Arrays.asList(attachment2, attachment1));

        return post;
    }
}