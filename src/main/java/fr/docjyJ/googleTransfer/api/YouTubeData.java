package fr.docjyJ.googleTransfer.api;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;

import static fr.docjyJ.googleTransfer.Lang.systemLog;
import static fr.docjyJ.googleTransfer.Lang.systemLogError;

public class YouTubeData {
    public static void transferLike(YouTube clientA, YouTube clientB, String pageToken,
                                    String type) throws IOException {

        VideoListResponse request = clientA.videos()
                .list("id")
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .setMyRating(type)
                .execute();
        for( Video value : request.getItems()) {
            try {
                systemLog(value.getId());
                clientB.videos().rate(value.getId(), type).execute();
            }
            catch (GoogleJsonResponseException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferLike(clientA, clientB, request.getNextPageToken(), type);
        }
    }
    public static void transferSubscriptions(YouTube clientA, YouTube clientB, String pageToken) throws IOException {
        SubscriptionListResponse request = clientA.subscriptions()
                .list("snippet")
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .setMine(true)
                .execute();
        for( Subscription value : request.getItems()) {
            try {
                systemLog(value.getId());
                clientB.subscriptions().insert("snippet",
                        new Subscription().setSnippet(
                                new SubscriptionSnippet()
                                        .setResourceId(value.getSnippet().getResourceId())))
                        .execute();
            }
            catch (GoogleJsonResponseException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferSubscriptions(clientA, clientB, request.getNextPageToken());
        }
    }
    public static void transferPlaylist(YouTube clientA, YouTube clientB, String pageToken) throws IOException {
        PlaylistListResponse request = clientA.playlists()
                .list("id,snippet,contentDetails")
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .setMine(true)
                .execute();
        for( Playlist value : request.getItems()) {
            try {
                transferPlaylistItem( clientA, clientB, "", value.getId(),
                        clientB.playlists().insert("snippet,status",
                                new Playlist()
                                        .setSnippet(new PlaylistSnippet()
                                                .setDefaultLanguage(value.getSnippet().getDefaultLanguage())
                                                .setDescription(value.getSnippet().getDescription())
                                                .setTags(value.getSnippet().getTags())
                                                .setTitle(value.getSnippet().getTitle()))
                                        .setStatus(new PlaylistStatus()
                                                .setPrivacyStatus(value.getStatus().getPrivacyStatus())))
                                .execute().getId());
            }
            catch (GoogleJsonResponseException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferPlaylist(clientA, clientB, request.getNextPageToken());
        }
    }

    public static void transferPlaylistItem(YouTube clientA, YouTube clientB, String pageToken,
                                            String PlaylistIdA,String PlaylistIdB) throws IOException {
        PlaylistItemListResponse request = clientA.playlistItems()
                .list("snippet")
                .setPlaylistId(PlaylistIdA)
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .execute();
        for( PlaylistItem value : request.getItems()) {
            try {
                systemLog(value.getId());
                clientB.playlistItems().insert("snippet",
                        new PlaylistItem().setSnippet(
                                new PlaylistItemSnippet()
                                        .setPlaylistId(PlaylistIdB)
                                        .setPosition(value.getSnippet().getPosition())
                                        .setResourceId(value.getSnippet().getResourceId())))
                        .execute();
            }
            catch (GoogleJsonResponseException e){
                systemLogError(e);
            }
        }
        if(request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            transferPlaylistItem(clientA, clientB, request.getNextPageToken(), PlaylistIdA, PlaylistIdB);
        }
    }

}
