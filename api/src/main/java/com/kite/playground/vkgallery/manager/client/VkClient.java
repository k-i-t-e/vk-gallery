package com.kite.playground.vkgallery.manager.client;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kite.playground.vkgallery.entity.Group;
import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.entity.vo.GalleryResult;
import com.kite.playground.vkgallery.manager.AuthManager;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.groups.responses.GetExtendedResponse;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.queries.groups.GroupsGetFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * A service class, that wraps library VkApiClient to perform required API operations.
 * Ensures request limits per second won't be exceeded by using a semaphore and a timer.
 */
@Service
@Slf4j
public class VkClient {
    private static final int MAX_ALLOWED_POSTS_COUNT = 100;
    private static final int MAX_ALLOWED_GROUPS_COUNT = 1000;
    private final AuthManager authManager;
    private final VkApiClient client;

    private final Timer requestTimer = new Timer(true);
    private final Semaphore requestSemaphore;

    @Autowired
    public VkClient(AuthManager authManager,
                    VkApiClient client,
                    @Value("${vk.api.refresh.period:1000}") long refreshPeriod,
                    @Value("${vk.api.max.requests.per.period:5}") int maxRequests) {
        this.authManager = authManager;
        this.client = client;
        requestSemaphore = new Semaphore(maxRequests);

        requestTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestSemaphore.drainPermits();
                requestSemaphore.release(maxRequests);
            }
        }, refreshPeriod, refreshPeriod);
    }

    public GalleryResult loadImages(String groupId, int pageSize, int offset) {
        UserActor userActor = new UserActor(authManager.getCurrentUser().getId().intValue(),
                                            authManager.getAccessToken());

        try {
            Pair<List<Image>, Integer> imagesAndOffset = loadImagePortion(groupId, userActor, pageSize, offset);
            List<Image> images = imagesAndOffset.getLeft();

            int nextPageOffset = offset;
            while (imagesAndOffset.getLeft().size() < pageSize) {
                Pair<List<Image>, Integer> portion = loadImagePortion(groupId, userActor, MAX_ALLOWED_POSTS_COUNT,
                        offset + imagesAndOffset.getRight());
                images.addAll(portion.getLeft());
                nextPageOffset += portion.getRight();
            }

            if (images.size() > pageSize) {
                List<Image> sublist = images.subList(0, pageSize);
                nextPageOffset -= images.subList(pageSize - 1, images.size()).stream()
                        .map(Image::getPostId)
                        .distinct()
                        .count();
                return new GalleryResult(sublist, nextPageOffset);
            } else {
                return new GalleryResult(images, nextPageOffset);
            }
        } catch (ApiException | ClientException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<List<Image>, Integer> loadImagePortion(String groupId, UserActor userActor, int pageSize, int offset)
            throws InterruptedException, ApiException, ClientException {
        requestSemaphore.acquire();

        log.debug("Loading wall posts portion of size {}, offset {}", pageSize, offset);
        List<WallPostFull> posts = client.wall().get(userActor)
            .domain(groupId)
            .count(pageSize)
            .offset(offset)
            .execute().getItems();

        List<Image> images = posts.stream()
                .filter(p -> p.getAttachments() != null)
                .flatMap(p -> p.getAttachments().stream()
                        .filter(a -> a.getType() == WallpostAttachmentType.PHOTO)
                        .map(a -> new ImmutablePair<>(p, a)))
                .map(pair -> {
                    Map<String, String> urls = Stream.of(
                            new ImmutablePair<>("75", pair.getRight().getPhoto().getPhoto75()),
                            new ImmutablePair<>("130", pair.getRight().getPhoto().getPhoto130()),
                            new ImmutablePair<>("604", pair.getRight().getPhoto().getPhoto604()),
                            new ImmutablePair<>("807", pair.getRight().getPhoto().getPhoto807()),
                            new ImmutablePair<>("1280", pair.getRight().getPhoto().getPhoto1280()),
                            new ImmutablePair<>("2560", pair.getRight().getPhoto().getPhoto2560())
                    )
                            .collect(Collectors.toMap(ImmutablePair::getLeft,
                                    p -> p.getRight() == null ? "" : p.getRight()));

                    return new Image(pair.getLeft().getId().longValue(), urls);
                })
                .collect(Collectors.toList());

        return new ImmutablePair<>(images, posts.size());
    }

    public Group getGroup(String groupId) {
        UserActor userActor = new UserActor(authManager.getCurrentUser().getId().intValue(),
                                            authManager.getAccessToken());

        try {
            requestSemaphore.acquire();
            List<GroupFull> groups = client.groups().getById(userActor).groupId(groupId).execute();
            return groups.stream()
                .findFirst()
                .map(vkGroup -> {
                    Group group = new Group();

                    group.setId(vkGroup.getId().longValue());
                    group.setDomain(vkGroup.getScreenName());
                    group.setName(vkGroup.getName());
                    group.setAlias(vkGroup.getName());

                    return group;
                })
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        } catch (InterruptedException | ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Group> getUsersGroups() {
        UserActor userActor = new UserActor(authManager.getCurrentUser().getId().intValue(),
                                            authManager.getAccessToken());

        try {
            requestSemaphore.acquire();
            GetExtendedResponse groupResponse = getGroupPortion(userActor, 0);

            List<Group> groups = groupResponse.getItems().stream()
                .map(this::convertGroup).collect(Collectors.toList());

            if (groupResponse.getCount() > MAX_ALLOWED_GROUPS_COUNT) {
                groups.addAll(IntStream.range(1, (int) Math.ceil(groupResponse.getCount() / MAX_ALLOWED_GROUPS_COUNT) + 1)
                    .mapToObj(i -> getGroupPortion(userActor, MAX_ALLOWED_GROUPS_COUNT * i))
                    .flatMap(resp -> resp.getItems().stream().map(this::convertGroup))
                    .collect(Collectors.toList()));
            }

            return groups;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Group convertGroup(GroupFull vkGroup) {
        Group group = new Group();

        group.setId(vkGroup.getId().longValue());
        group.setName(vkGroup.getName());
        group.setAlias(vkGroup.getName());
        group.setDomain(vkGroup.getScreenName());
        group.setImageUrl(vkGroup.getPhoto100());

        return group;
    }

    private GetExtendedResponse getGroupPortion(UserActor userActor, int offset) {
        try {
            return client.groups().getExtended(userActor)
                .filter(GroupsGetFilter.ADMIN,
                        GroupsGetFilter.EDITOR,
                        GroupsGetFilter.MODER,
                        GroupsGetFilter.EVENTS,
                        GroupsGetFilter.GROUPS,
                        GroupsGetFilter.PUBLICS)
                .count(MAX_ALLOWED_GROUPS_COUNT)
                .offset(offset)
                .execute();
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
