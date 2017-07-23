package com.baskoroaji.stage2.Utils;

import android.net.Uri;
import android.util.Log;

import com.baskoroaji.stage2.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Macpro on 6/28/17.
 */

public final class NetworkUtils {

    public static final String SORT_POPULAR = "popular";
    public static final String SORT_TOPRATED = "top_rated";
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185//";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String APIKEY_URL = "api_key";
    private static final String PAGE = "page";

    public static URL buildUrl(String sortMethod, int paging){
        int page = (paging + 20) / 20;
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortMethod)
                .appendQueryParameter(PAGE,Integer.toString(page))
                .appendQueryParameter(APIKEY_URL, BuildConfig.MOVIE_DB_API_TOKEN).build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v("URL Build", "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return  scanner.next();
            }else{
                return  null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

}
