package com.baskoroaji.stage2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baskoroaji.stage2.adapters.ReviewListAdapter;
import com.baskoroaji.stage2.adapters.VideoListAdapter;
import com.baskoroaji.stage2.data.MovieContentProvider;
import com.baskoroaji.stage2.data.MovieReaderContract;
import com.baskoroaji.stage2.data.MovieReaderDBHelper;
import com.baskoroaji.stage2.models.Movie;
import com.baskoroaji.stage2.models.Review;
import com.baskoroaji.stage2.models.Video;
import com.baskoroaji.stage2.utils.MovieJsonParser;
import com.baskoroaji.stage2.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class DetailActivity extends AppCompatActivity implements VideoListAdapter.ClickRecyclerListener{

    private Movie mData;
    private TextView mMovieTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mOverViewTextView;
    private ImageView mPosterImageView;
    private Button mFavouriteButton;
    private Button mRemoveFavouriteButton;

    private RecyclerView mVideoRecyclerView;
    private VideoListAdapter mVideoAdapter;

    private RecyclerView mReviewRecyclerView;
    private ReviewListAdapter mReviewAdapter;

    private List<Video> videosData;
    private  MovieContentProvider movieContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        movieContentProvider = new MovieContentProvider();
        movieContentProvider.setContext(this);

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movietittle_detailspage);
        mYearTextView = (TextView) findViewById(R.id.tv_year_detailpage);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating_detailpage);
        mOverViewTextView = (TextView) findViewById(R.id.tv_overview_detailpage);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_detailpage);

        mFavouriteButton = (Button) findViewById(R.id.btn_favourite);
        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putData();
            }
        });

        mRemoveFavouriteButton = (Button) findViewById(R.id.btn_removefavourite);
        mRemoveFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        mVideoRecyclerView = (RecyclerView) findViewById(R.id.rv_videolist);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVideoRecyclerView.setLayoutManager(linearLayoutManager);
        mVideoRecyclerView.setHasFixedSize(true);
        mVideoAdapter = new VideoListAdapter(this);
        mVideoRecyclerView.setAdapter(mVideoAdapter);


        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviewlist);
        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(linearLayoutManager2);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewListAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);
       // mContext = sa

        Intent i = getIntent();
        if (i.hasExtra(MainActivity.DATA_IDENTIFIER)){
            mData = (Movie) i.getSerializableExtra(MainActivity.DATA_IDENTIFIER);
            mMovieTitleTextView.setText(mData.title);
            mYearTextView.setText(mData.GetYYYMMDD());
            mRatingTextView.setText(mData.vote_average.toString());
            String imgUrl = NetworkUtils.BASE_URL_IMAGE + mData.imgPoster;
            Picasso.with(this).load(imgUrl).into(mPosterImageView);
            mOverViewTextView.setText(mData.overView);
        }



        onRequestVideo(NetworkUtils.buildUrlGeneral(new String[]{Integer.toString(mData.id), NetworkUtils.VIDEOSPARAM}));
        String[] paths = {Integer.toString(mData.id), NetworkUtils.REVIEWPARAM};
        String reviewUrl = NetworkUtils.buildUrlGeneral(paths);
        onRequestReview(reviewUrl);

        hideButtonFavouriteStatus();
    }


    @Override
    public void onClickItem(int index) {

        watchYoutubeVideo(videosData.get(index).key);
    }

    void onRequestReview(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response review ",""+response);
                        try{

                           mReviewAdapter.setData( MovieJsonParser.deserializeJsonReview(response));
                        }
                        catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    void onRequestVideo(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response video ",""+response);

                        try {
                            videosData = MovieJsonParser.deserializeJsonVideo(response);
                            mVideoAdapter.SetVideoData(videosData);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    void hideButtonFavouriteStatus(){
        boolean isFavorite = isMovieFavorite();

        if (isFavorite){
            //show button remove favorite
            mRemoveFavouriteButton.setVisibility(View.VISIBLE);
            mFavouriteButton.setVisibility(View.INVISIBLE);
        }
        else{
            //show button add to favorite
            mRemoveFavouriteButton.setVisibility(View.INVISIBLE);
            mFavouriteButton.setVisibility(View.VISIBLE);
        }
    }

    boolean isMovieFavorite(){
        Cursor cursor = movieContentProvider.query(null,null, MovieReaderContract.MovieEntry.COLUMN_NAME_ID + " = ?", new String[]{Integer.toString(mData.id)}, null);

        if (cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        else{
            cursor.close();
            return true;
        }
    }

    void putData(){
        ContentValues values = new ContentValues();
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE, mData.title);
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_ID, Integer.toString(mData.id));
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_IMGPOSTER, mData.imgPoster);
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_OVERVIEW, mData.overView);
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_VOTE, Double.toString(mData.vote_average));
        values.put(MovieReaderContract.MovieEntry.COLUMN_NAME_RELEASEDATE, mData.GetYYYMMDD());

        movieContentProvider.insert(null,values);
        hideButtonFavouriteStatus();
    }

    void deleteData(){
        String selection = MovieReaderContract.MovieEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { Integer.toString(mData.id) };
        movieContentProvider.delete(null,selection,selectionArgs);
        hideButtonFavouriteStatus();
    }

}
