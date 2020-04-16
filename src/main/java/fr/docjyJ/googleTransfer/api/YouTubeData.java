package fr.docjyJ.googleTransfer.api;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;

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
            clientB.videos().rate(value.getId(), type ).execute();
            //A Delete
            System.out.print(value.getId());
            clientA.videos().rate(value.getId(), "none").execute();
        }
        if(!request.getNextPageToken().isEmpty()){
            transferLike(clientA, clientB, type, request.getNextPageToken());
        }
    }

    public static void transferPlaylist(YouTube clientA, YouTube clientB, String pageToken) throws IOException {
        PlaylistListResponse request = clientA.playlists()
                .list("id")
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .setMine(true)
                .execute();
        for( Playlist value : request.getItems()) {
            //create playlist
            transferPlaylistItem( clientA, clientB, "", value.getId(),"Null");
        }
        if(!request.getNextPageToken().isEmpty()){
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
                clientB.playlistItems().insert("snippet",
                        new PlaylistItem().setSnippet(
                                new PlaylistItemSnippet()
                                        .setPlaylistId(PlaylistIdB)
                                        .setResourceId(value.getSnippet().getResourceId())))
                        .execute();
        }
        if(!request.getNextPageToken().isEmpty()){
            transferPlaylistItem(clientA, clientB, request.getNextPageToken(), PlaylistIdA, PlaylistIdB);
        }
    }
}
