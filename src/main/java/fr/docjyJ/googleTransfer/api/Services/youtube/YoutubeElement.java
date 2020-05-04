package fr.docjyJ.googleTransfer.api.Services.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import fr.docjyJ.googleTransfer.api.Utils.GoogleTransfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeElement extends GoogleTransfer {
    //ELEMENT
    protected transient YouTube service;
    protected List<String> likes;
    protected List<String> dislikes;
    protected List<Subscription> subscriptions;
    protected List<PlaylistElement> playlists;

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
                    .list("id")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMyRating("like")
                    .execute();
            for( Video value : request.getItems()) {
                this.likes.add(value.getId());
            }
        }
        return this;
    }
    public YoutubeElement readDislike() throws IOException {
        this.dislikes = new ArrayList<>();
        VideoListResponse request = new VideoListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()) {
            request = this.service.videos()
                    .list("id")
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .setMyRating("dislike")
                    .execute();
            for( Video value : request.getItems()) {
                this.dislikes.add(value.getId());
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
                this.subscriptions.add(new Subscription().setSnippet(
                        new SubscriptionSnippet()
                                .setResourceId(value.getSnippet().getResourceId())));
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
                this.playlists.add(new PlaylistElement(value, this.service));
            }
        }
        return this;
    }

    //PUT
    public YoutubeElement putAll(YouTube newClient) throws IOException {
        return this.putLike(newClient).putDislike(newClient).putSubscriptions(newClient).putPlaylist(newClient);
    }
    public YoutubeElement putLike(YouTube newClient) throws IOException {
        for (String id :this.likes) {
            logPrint(id);
            newClient.videos().rate(id, "like").execute();
        }
        return this;
    }
    public YoutubeElement putDislike(YouTube newClient) throws IOException {
        for (String id :this.dislikes) {
            logPrint(id);
            newClient.videos().rate(id, "dislike").execute();
        }
        return this;
    }
    public YoutubeElement putSubscriptions(YouTube newClient) throws IOException {
        for (Subscription subscription :this.subscriptions) {
            logPrint(subscription);
            newClient.subscriptions().insert("snippet",subscription).execute();
        }
        return this;
    }
    public YoutubeElement putPlaylist(YouTube newClient) throws IOException {
        for (PlaylistElement playlist: this.playlists) {
            logPrint(playlist.playlist);
            String id = newClient.playlists()
                    .insert("snippet,status",playlist.playlist)
                    .execute()
                    .getId();
            for (PlaylistItem item :playlist.items){
                logPrint(item);
                item.getSnippet().setPlaylistId(id);
                newClient.playlistItems()
                        .insert("snippet",item)
                        .execute();
            }
        }
        return this;
    }

    //GET
    public YouTube getService() {
        return service;
    }
    public List<String> getLikes() {
        return likes;
    }
    public List<String> getDislikes() {
        return dislikes;
    }
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
    public List<PlaylistElement> getPlaylists() {
        return playlists;
    }
}

