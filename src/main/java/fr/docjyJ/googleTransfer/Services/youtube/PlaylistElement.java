package fr.docjyJ.googleTransfer.Services.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistElement {
    //ELEMENT
    protected Playlist playlist;
    protected List<PlaylistItem> items;

    //CONSTRUCTOR
    protected PlaylistElement(Playlist playlist, YouTube client) throws IOException {
        this.playlist = new Playlist()
                .setSnippet(new PlaylistSnippet()
                        .setDefaultLanguage(playlist.getSnippet().getDefaultLanguage())
                        .setDescription(playlist.getSnippet().getDescription())
                        .setTags(playlist.getSnippet().getTags())
                        .setTitle(playlist.getSnippet().getTitle()))
                .setStatus(new PlaylistStatus()
                        .setPrivacyStatus(playlist.getStatus().getPrivacyStatus()));
        this.items = new ArrayList<>();
        PlaylistItemListResponse request = new PlaylistItemListResponse().setNextPageToken(" ");
        while (request.getNextPageToken()!=null && !request.getNextPageToken().isEmpty()){
            request = client.playlistItems()
                    .list("snippet")
                    .setPlaylistId(playlist.getId())
                    .setMaxResults(50L)
                    .setPageToken(request.getNextPageToken())
                    .execute();
            for( PlaylistItem value : request.getItems()) {
                this.items.add(new PlaylistItem().setSnippet(
                        new PlaylistItemSnippet()
                                .setPosition(value.getSnippet().getPosition())
                                .setResourceId(value.getSnippet().getResourceId())));
            }
        }
    }

    //GET
    public Playlist getPlaylist() {
        return playlist;
    }
    public List<PlaylistItem> getItems() {
        return items;
    }
}
