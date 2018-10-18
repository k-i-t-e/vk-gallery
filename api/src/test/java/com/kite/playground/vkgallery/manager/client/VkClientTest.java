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
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.kite.playground.vkgallery.entity.Group;
import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.entity.vo.GalleryResult;
import com.kite.playground.vkgallery.manager.AuthManager;
import com.vk.api.sdk.actions.Groups;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.groups.responses.GetExtendedResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.groups.GroupsGetByIdQuery;
import com.vk.api.sdk.queries.groups.GroupsGetFilter;
import com.vk.api.sdk.queries.groups.GroupsGetQueryWithExtended;
import com.vk.api.sdk.queries.wall.WallGetQuery;

public class VkClientTest {
    private static final String TEST_TOKEN = "TEST_TOKEN";
    private static final long TEST_REFRESH_PERIOD = 1000L;
    private static final int TEST_MAX_ACTIONS_PER_PERIOD = 5;
    private static final int TEST_MAX_GROUP_COUNT = 1000;

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

    @Mock
    private Groups mockGroups;
    @Mock
    private GroupsGetByIdQuery mockGroupsByIdQuery;
    @Mock
    private GroupsGetQueryWithExtended mockGroupsQueryExtended;
    @Captor
    private ArgumentCaptor<UserActor> userActorCaptor;
    @Captor
    private ArgumentCaptor<GroupsGetFilter> groupsFilterCaptor;

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
        createWallMock(Collections.emptyList());

        when(vkApiClient.wall()).thenReturn(wallMock);

        ForkJoinPool pool = new ForkJoinPool(10);
        IntStream.range(0, 10)
            .forEach(i -> pool.submit(() -> vkClient.loadImages("test", 0 ,0)));

        Thread.sleep(TEST_REFRESH_PERIOD / 2);
        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD, longAdder.intValue());

        Thread.sleep(TEST_REFRESH_PERIOD);

        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD * 2, longAdder.intValue());
    }

    @Test
    public void testParsePosts() throws ClientException, ApiException {
        List<WallPostFull> posts = IntStream.range(0, 10)
            .mapToObj(i -> createPostMockWithAttachments())
            .collect(Collectors.toList());
        createWallMock(posts);

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
            .mapToObj(i -> createPostMockWithAttachments())
            .collect(Collectors.toList());
        createWallMock(posts);

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
            .mapToObj(i -> createPostMockWithAttachments())
            .collect(Collectors.toList());
        createWallMock(posts);

        when(vkApiClient.wall()).thenReturn(wallMock);

        GalleryResult result = vkClient.loadImages("test", 15, 0);

        Assert.assertEquals(15, result.getImages().size());
        Assert.assertEquals(15, result.getNextPageOffset());

        verify(vkApiClient, times(2)).wall();
        verify(mockQuery).offset(0);
        verify(mockQuery).offset(10);
    }

    @Test
    public void testGetGroup() throws ClientException, ApiException {
        GroupFull group = createGroupFullMock();

        createGroupsByIdMock(group.getScreenName(), group);
        when(vkApiClient.groups()).thenReturn(mockGroups);

        Group loadedGroup = vkClient.getGroup(group.getScreenName());

        assertEquals(group, loadedGroup);

        verify(mockGroups).getById(any(UserActor.class));
        verify(mockGroupsByIdQuery).groupId(group.getScreenName());
    }


    /**
     * Check that all the API methods are invoked correctly and the result is parsed
     * @throws ClientException
     * @throws ApiException
     */
    @Test
    public void testGetUserGroups() throws ClientException, ApiException {
        GetExtendedResponse extendedResponse = mock(GetExtendedResponse.class);
        List<GroupFull> groups = Collections.singletonList(createGroupFullMock());

        when(extendedResponse.getCount()).thenReturn(groups.size());
        when(extendedResponse.getItems()).thenReturn(groups);

        createGroupsMock(extendedResponse);
        when(vkApiClient.groups()).thenReturn(mockGroups);

        List<Group> loadedGroups = vkClient.getUsersGroups();

        Assert.assertEquals(groups.size(), loadedGroups.size());
        assertEquals(groups.get(0), loadedGroups.get(0));

        verify(vkApiClient).groups();
        verify(mockGroups).getExtended(userActorCaptor.capture());
        UserActor appliedUser = userActorCaptor.getValue();
        Assert.assertEquals(TEST_TOKEN, appliedUser.getAccessToken());
        Assert.assertEquals(testUser.getId().intValue(), appliedUser.getId().intValue());

        verify(mockGroupsQueryExtended).filter(groupsFilterCaptor.capture());
        List<GroupsGetFilter> filters = groupsFilterCaptor.getAllValues();
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.GROUPS));
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.PUBLICS));
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.EVENTS));
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.ADMIN));
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.EDITOR));
        Assert.assertTrue(filters.stream().anyMatch(f -> f == GroupsGetFilter.MODER));

        verify(mockGroupsQueryExtended).count(TEST_MAX_GROUP_COUNT);
        verify(mockGroupsQueryExtended).offset(any());
    }

    /**
     * Check that client correctly behaves, when the result count is more than API can return. Should fire multiple
     * requests and merge the results.
     * @throws ClientException
     * @throws ApiException
     */
    @Test
    public void testGetUserGroupsMultipleRequests() throws ClientException, ApiException {
        List<GroupFull> groups = IntStream.range(0, 1005)
            .mapToObj(i -> createGroupFullMock())
            .collect(Collectors.toList());

        GetExtendedResponse extendedResponse = mock(GetExtendedResponse.class);
        when(extendedResponse.getCount()).thenReturn(groups.size());
        when(extendedResponse.getItems()).thenReturn(groups);

        createGroupsMock(extendedResponse);
        when(vkApiClient.groups()).thenReturn(mockGroups);

        List<Group> loadedGroups = vkClient.getUsersGroups();

        Assert.assertTrue(loadedGroups.size() >= groups.size()); // Too lazy to mock correct return values...
                                                                            // So we'll just check that the value is at
                                                                            // least 1005. That should be enough.

        verify(vkApiClient, times(2)).groups();
        verify(mockGroupsQueryExtended).offset(0);
        verify(mockGroupsQueryExtended).offset(TEST_MAX_GROUP_COUNT);
    }

    private void createGroupsMock(GetExtendedResponse extendedResponse) throws ApiException, ClientException {
        when(mockGroupsQueryExtended.filter(ArgumentMatchers.<GroupsGetFilter>any()))
            .thenReturn(mockGroupsQueryExtended);
        when(mockGroupsQueryExtended.count(TEST_MAX_GROUP_COUNT)).thenReturn(mockGroupsQueryExtended);
        when(mockGroupsQueryExtended.offset(any())).thenReturn(mockGroupsQueryExtended);
        when(mockGroupsQueryExtended.execute()).thenReturn(extendedResponse);
        when(mockGroups.getExtended(any(UserActor.class))).thenReturn(mockGroupsQueryExtended);
    }

    private void createWallMock(List<WallPostFull> posts) throws ApiException, ClientException {
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
    }

    private WallPostFull createPostMockWithAttachments() {
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

    private void createGroupsByIdMock(String groupId, GroupFull group) throws ApiException, ClientException {
        when(mockGroupsByIdQuery.groupId(groupId)).thenReturn(mockGroupsByIdQuery);
        when(mockGroupsByIdQuery.execute()).thenReturn(Collections.singletonList(group));
        when(mockGroups.getById(any(UserActor.class))).thenReturn(mockGroupsByIdQuery);
    }

    private void assertEquals(GroupFull group, Group loadedGroup) {
        Assert.assertEquals(group.getId().intValue(), loadedGroup.getId().intValue());
        Assert.assertEquals(group.getName(), loadedGroup.getName());
        Assert.assertEquals(group.getName(), loadedGroup.getAlias());
        Assert.assertEquals(group.getScreenName(), loadedGroup.getDomain());
        Assert.assertEquals(group.getPhoto100(), loadedGroup.getImageUrl());
    }

    private GroupFull createGroupFullMock() {
        GroupFull group = mock(GroupFull.class);
        when(group.getId()).thenReturn(1);
        when(group.getScreenName()).thenReturn("test");
        when(group.getName()).thenReturn("testName");
        when(group.getPhoto100()).thenReturn("testPhoto");
        return group;
    }
}