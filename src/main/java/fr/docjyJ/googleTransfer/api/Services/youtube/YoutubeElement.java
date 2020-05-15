package fr.docjyJ.googleTransfer.api.Services.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;
import fr.docjyJ.googleTransfer.api.Utils.TemplateObject;
import fr.docjyJ.googleTransfer.api.Utils.TemplateObjectContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeElement extends GoogleTransfer {
    //ELEMENT
    protected transient YouTube service;
    protected List<TemplateObject<String>> likes;
    protected List<TemplateObject<String>> dislikes;
    protected List<TemplateObject<Subscription>> subscriptions;
    protected List<TemplateObjectContent<Playlist,PlaylistItem>> playlists;

    //CONSTRUCTOR
    public YoutubeElement(YouTube service) {
        this.service = service;
    }

    //READ
    public YoutubeElement readAll() throws IOException {
        return this.readLike().readDislike().readSubscriptions().readPlaylist();
    }
    public YoutubeElement readLike() throws IOException {
        this.likes = new ArrayList<>();
        VideoListResponse request = new VideoListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            request = this.service.videos()
                    .list("snippet")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMyRating("like")
                    .execute();
            for( Video value : request.getItems()) {
                logPrint("READ","like",value.getSnippet().getTitle());
                this.likes.add(new TemplateObject<>(
                        value.getId(),
                        value.getSnippet().getTitle(),
                        value.getId()));
            }
        }
        return this;
    }
    public YoutubeElement readDislike() throws IOException {
        this.dislikes = new ArrayList<>();
        VideoListResponse request = new VideoListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            request = this.service.videos()
                    .list("snippet")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMyRating("dislike")
                    .execute();
            for( Video value : request.getItems()) {
                logPrint("READ","dislike",value.getSnippet().getTitle());
                this.dislikes.add(new TemplateObject<>(
                        value.getId(),
                        value.getSnippet().getTitle(),
                        value.getId()));
            }
        }
        return this;
    }
    public YoutubeElement readSubscriptions() throws IOException {
        this.subscriptions = new ArrayList<>();
        SubscriptionListResponse request = new SubscriptionListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            request = this.service.subscriptions()
                    .list("snippet")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMine(true)
                    .execute();
            for (Subscription value : request.getItems()) {
                logPrint("READ","subscription",value.getSnippet().getTitle());
                this.subscriptions.add(new TemplateObject<>(
                        value.getSnippet().getResourceId().getChannelId(),
                        value.getSnippet().getTitle(),
                        new Subscription().setSnippet(
                                new SubscriptionSnippet()
                                        .setResourceId(value.getSnippet().getResourceId()))
                ));
            }
        }
        return this;
    }
    public YoutubeElement readPlaylist() throws IOException {
        this.playlists = new ArrayList<>();
        PlaylistListResponse request = new PlaylistListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            request = this.service.playlists()
                    .list("snippet,status")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMine(true)
                    .execute();
            for( Playlist value : request.getItems()) {
                logPrint("READ", "playlist",value.getSnippet().getTitle());
                List<TemplateObject<PlaylistItem>> content = new ArrayList<>();
                PlaylistItemListResponse request2 = new PlaylistItemListResponse().setNextPageToken(" ");
                while (request2.getNextPageToken()!=null && !request2.getNextPageToken().isEmpty()){
                    request2 = this.service.playlistItems()
                            .list("snippet")
                            .setPlaylistId(value.getId())
                            .setMaxResults(50L)
                            .setPageToken(request2.getNextPageToken())
                            .execute();
                    for( PlaylistItem value2 : request2.getItems()) {
                        logPrint("READ", "playlist",value.getSnippet().getTitle(),
                                value2.getSnippet().getTitle());
                        content.add(new TemplateObject<>(
                                value2.getId(),
                                value2.getSnippet().getTitle(),
                                new PlaylistItem().setSnippet(
                                        new PlaylistItemSnippet()
                                                .setPosition(value2.getSnippet().getPosition())
                                                .setResourceId(value2.getSnippet().getResourceId()))));
                    }
                }
                this.playlists.add(new TemplateObjectContent<>(
                        value.getId(),
                        value.getSnippet().getTitle(),
                        new Playlist()
                                .setSnippet(new PlaylistSnippet()
                                        .setDefaultLanguage(value.getSnippet().getDefaultLanguage())
                                        .setDescription(value.getSnippet().getDescription())
                                        .setTags(value.getSnippet().getTags())
                                        .setTitle(value.getSnippet().getTitle()))
                                .setStatus(new PlaylistStatus()
                                        .setPrivacyStatus(value.getStatus().getPrivacyStatus())),
                        content));
            }
        }
        return this;
    }

    //PUT
    public YoutubeElement putAll(YoutubeElement data) throws IOException {
        return this
                .putLike(data.likes)
                .putDislike(data.dislikes)
                .putSubscriptions(data.subscriptions)
                .putPlaylist(data.playlists);
    }
    public YoutubeElement putLike(List<TemplateObject<String>> data) throws IOException {
        for (TemplateObject<String> value :data) {
            logPrint("PUT","like",value.getName());
            service.videos().rate(value.getObject(), "like").execute();
        }
        return this;
    }
    public YoutubeElement putDislike(List<TemplateObject<String>> data) throws IOException {
        for (TemplateObject<String> value :data) {
            logPrint("PUT","dislike",value.getName());
            service.videos().rate(value.getObject(), "dislike").execute();
        }
        return this;
    }
    public YoutubeElement putSubscriptions(List<TemplateObject<Subscription>> data) throws IOException {
        for (TemplateObject<Subscription> value :data) {
            logPrint("PUT","subscription",value.getName());
            service.subscriptions().insert("snippet", value.getObject()).execute();
        }
        return this;
    }
    public YoutubeElement putPlaylist(List<TemplateObjectContent<Playlist,PlaylistItem>> data) throws IOException {
        for (TemplateObjectContent<Playlist,PlaylistItem> playlist: data) {
            logPrint("PUT", "playlist", playlist.getName());
            String id = service.playlists()
                    .insert("snippet,status", playlist.getObject())
                    .execute()
                    .getId();
            for (TemplateObject<PlaylistItem> item :playlist.getContent()){
                logPrint("PUT", "playlist",playlist.getName(),item.getName());
                PlaylistItem temp = item.getObject();
                temp.getSnippet().setPlaylistId(id);
                service.playlistItems()
                        .insert("snippet", temp)
                        .execute();
            }
        }
        return this;
    }

    //GET
    public YouTube getService() {
        return service;
    }
    public List<TemplateObject<String>> getLikes() {
        return likes;
    }
    public List<TemplateObject<String>> getDislikes() {
        return dislikes;
    }
    public List<TemplateObject<Subscription>> getSubscriptions() {
        return subscriptions;
    }
    public List<TemplateObjectContent<Playlist,PlaylistItem>> getPlaylists() {
        return playlists;
    }
}

