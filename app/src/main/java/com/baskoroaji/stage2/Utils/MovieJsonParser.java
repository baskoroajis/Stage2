package com.baskoroaji.stage2.Utils;

import com.baskoroaji.stage2.Models.Movie;

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

}
