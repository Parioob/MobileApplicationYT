package fr.esilv.tourailleytapi;

/**
 * Created by pierr on 26/02/2016.
 */
import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class YoutubeConnection {

    private YouTube youtube;
    private YouTube.Search.List query;

    public static final String KEY = "AIzaSyAXBUYNxjkG1unEuMY2BOaj0W3FnvLLDzI";

    public YoutubeConnection(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try
        {
            //On crée ici notre Query
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");

        }
        catch(IOException e){
            Log.d("YC", "Could not initialize: " + e);
        }
    }

    public List<Video> search(String s){
        query.setQ(s);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<Video> items = new ArrayList<Video>();
            for(SearchResult result:results){
                Video item = new Video();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        }
        catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }

}