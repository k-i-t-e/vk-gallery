package com.kite.playground.vkgallery.manager.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.kite.playground.vkgallery.entity.VkUser;
import com.kite.playground.vkgallery.manager.AuthManager;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetQuery;

public class VkClientTest {
    private static final String TEST_TOKEN = "TEST_TOKEN";
    private static final long TEST_REFRESH_PERIOD = 2000L;
    private static final int TEST_MAX_ACTIONS_PER_PERIOD = 5;

    @Mock
    private AuthManager authManager;
    @Mock
    private VkApiClient vkApiClient;

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
    public void testOnly5ActionsPerSecond() throws InterruptedException, ClientException, ApiException {
        Wall wallMock = mock(Wall.class);
        WallGetQuery mockQuery = mock(WallGetQuery.class);
        GetResponse mockResponse = mock(GetResponse.class);

        when(wallMock.get(Mockito.any(UserActor.class))).thenReturn(mockQuery);
        when(mockQuery.count(any())).thenReturn(mockQuery);
        when(mockQuery.domain(any())).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(mockResponse);
        when(mockResponse.getItems()).then(invocation -> {
            longAdder.increment();
            return Collections.emptyList();
        });

        when(vkApiClient.wall()).thenReturn(wallMock);

        ForkJoinPool pool = new ForkJoinPool(10);
        IntStream.range(0, 10).forEach(i -> pool.submit(() -> vkClient.loadImages("test", 0 ,0)));

        Thread.sleep(TEST_REFRESH_PERIOD / 2);
        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD, longAdder.intValue());

        Thread.sleep(TEST_REFRESH_PERIOD);

        Assert.assertEquals(TEST_MAX_ACTIONS_PER_PERIOD * 2, longAdder.intValue());
    }
}