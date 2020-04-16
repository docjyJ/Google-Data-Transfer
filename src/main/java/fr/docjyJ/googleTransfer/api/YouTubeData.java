package fr.docjyJ.googleTransfer.api;

import com.google.api.services.youtube.YouTube;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

public class YouTubeData {
    private static final Gson GSON = new Gson();

    private static class ResponseObject{
        @SerializedName("items")
        @Expose
        List<VideoObject> items;

        @SerializedName("nextPageToken")
        @Expose
        String nextPageToken;

        public List<VideoObject> getItems() {
            return items;
        }

        public String getNextPageToken() {
            return nextPageToken;
        }
    }
    private static class VideoObject{
        @SerializedName("id")
        @Expose
        String id;

        public String getId() {
            return id;
        }
    }

    public static void transferLike(YouTube clientA,
                                    YouTube clientB,
                                    String type,
                                    String pageToken) throws IOException {

        ResponseObject request = GSON.fromJson(String.valueOf(clientA.videos()
                .list("id")
                .setMaxResults(50L)
                .setPageToken(pageToken)
                .setMyRating(type)
                .execute()), ResponseObject.class);
        for( VideoObject value : request.getItems()) {
            System.out.print(value.getId());
            clientB.videos().rate(value.getId(), type ).execute();
            clientA.videos().rate(value.getId(), "none").execute();
        }
        if(!request.getNextPageToken().isEmpty()){
            transferLike(clientA, clientB, type, request.getNextPageToken());
        }
    }
}
