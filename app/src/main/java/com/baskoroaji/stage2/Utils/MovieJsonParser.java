package com.baskoroaji.stage2.utils;

import com.baskoroaji.stage2.models.Movie;
import com.baskoroaji.stage2.models.Review;
import com.baskoroaji.stage2.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Macpro on 6/30/17.
 */

public final class MovieJsonParser {
    public static List<Movie> serializeJson(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        JSONArray movieResults = result.getJSONArray("results");

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieResults.length(); i++){
            JSONObject movieJsonObject = movieResults.getJSONObject(i);
            Movie movie = new Movie(movieJsonObject.getInt("id")
                    ,movieJsonObject.getString("title")
                    ,movieJsonObject.getString("poster_path")
                    ,movieJsonObject.getString("overview")
                    ,movieJsonObject.getDouble("vote_average")
                    ,movieJsonObject.getString("release_date")
                    );
            movies.add(movie);
        }
        return movies;
    }

    public static  List<Video> deserializeJsonVideo(String json) throws  JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray movieResults = result.getJSONArray("results");

        List<Video> videos = new ArrayList<>();
        for (int i =0; i < movieResults.length(); i++){
            JSONObject videoJsonObject = movieResults.getJSONObject(i);
            Video video = new Video(videoJsonObject.getString("id"),
                    videoJsonObject.getString("key"),
                    videoJsonObject.getString("name"),
                    videoJsonObject.getString("site"));
            videos.add(video);
        }

        return videos;
    }

    public static  List<Review> deserializeJsonReview(String json) throws  JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray movieResults = result.getJSONArray("results");

        List<Review> reviews = new ArrayList<>();
        for (int i =0; i < movieResults.length(); i++){
            JSONObject videoJsonObject = movieResults.getJSONObject(i);
            Review review = new Review(videoJsonObject.getString("id"),
                    videoJsonObject.getString("author"),
                    videoJsonObject.getString("content"),
                    videoJsonObject.getString("url"));
            reviews.add(review);
        }

        return reviews;
    }

}
