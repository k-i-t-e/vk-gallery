package com.kite.playground.vkgallery.manager.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kite.playground.vkgallery.entity.Image;
import com.kite.playground.vkgallery.manager.AuthManager;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;

@Service
public class VkClient {
    private final AuthManager authManager;
    private VkApiClient client;

    @Autowired
    public VkClient(AuthManager authManager) {
        this.authManager = authManager;
        TransportClient transportClient = HttpTransportClient.getInstance();
        client = new VkApiClient(transportClient);
    }

    public List<Image> loadImages(String groupId) {
        UserActor userActor = new UserActor(authManager.getCurrentUser().getId().intValue(),
                                            authManager.getAccessToken());

        try {
            List<WallPostFull> posts = client.wall().get(userActor)
                .domain(groupId)
                .count(100)
                .execute().getItems();

            return posts.stream()
                .filter(p -> p.getAttachments() != null)
                .map(p -> new ImmutablePair<>(p, p.getAttachments()))
                .map(pair -> {
                    Map<String, String> urls = pair.getRight().stream()
                        .filter(a -> a.getType() == WallpostAttachmentType.PHOTO)
                        .flatMap(a -> Stream.of(new ImmutablePair<>("75", a.getPhoto().getPhoto75()),
                                            new ImmutablePair<>("130", a.getPhoto().getPhoto130()),
                                            new ImmutablePair<>("604", a.getPhoto().getPhoto604()),
                                            new ImmutablePair<>("807", a.getPhoto().getPhoto807()),
                                            new ImmutablePair<>("1280", a.getPhoto().getPhoto1280()),
                                            new ImmutablePair<>("2560", a.getPhoto().getPhoto2560())))
                        .collect(Collectors.toMap(ImmutablePair::getLeft,
                                                  p -> p.getRight() == null ? "" : p.getRight()));

                    return new Image(pair.getLeft().getId().longValue(), urls);
                })
                .collect(Collectors.toList());
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
