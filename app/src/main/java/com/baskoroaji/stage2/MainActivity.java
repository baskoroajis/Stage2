package com.baskoroaji.stage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.baskoroaji.stage2.adapters.MovieListAdapter;
import com.baskoroaji.stage2.data.MovieContentProvider;
import com.baskoroaji.stage2.data.MovieReaderContract;
import com.baskoroaji.stage2.data.MovieReaderDBHelper;
import com.baskoroaji.stage2.models.Movie;
import com.baskoroaji.stage2.utils.MovieJsonParser;
import com.baskoroaji.stage2.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ClickRecyclerListener{

    private List<Movie> mListMovie;
    private RecyclerView mRecyclerView;
    private MovieListAdapter movieListAdapter;
    private boolean mLoading = true;
    private ProgressBar mLoadingIndicator;

    public static final String DATA_IDENTIFIER = "DATA";
    private final String SORTBY_IDENTIFIER = "SORTBY";
    private final String MOVIES_KEY_SAVEDINSTANCE = "MOVIESDATA";

    private boolean isSortByPopular = true;
    private boolean isFavourite = false;

    private  MovieContentProvider movieContentProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieContentProvider = new MovieContentProvider();
        movieContentProvider.setContext(this);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loadingMovie);

        Log.d("Create!","");


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(movieListAdapter);

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(MOVIES_KEY_SAVEDINSTANCE)){
                mListMovie = (ArrayList<Movie>)savedInstanceState.getSerializable(MOVIES_KEY_SAVEDINSTANCE);
                movieListAdapter.setMovieData(mListMovie);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }
        else{
            if (!isFavourite)
                requestData(0);
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItem = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!mLoading && lastVisibleItem == totalItem - 1) {
                    if (!isFavourite)
                      requestData(totalItem);
                }
            }
        });

        if (isFavourite)
            getFavourite();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.action_sortbypopular:
                //request sort by popular
                isSortByPopular = true;
                isFavourite = false;
                mListMovie = null;
                requestData(0);
                break;
            case R.id.action_sortbyrates:
                //request sort by highest rate
                isSortByPopular = false;
                isFavourite = false;
                mListMovie = null;
                requestData(0);
                break;
            case R.id.action_sortbyfavourite:
                getFavourite();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIES_KEY_SAVEDINSTANCE, new ArrayList(mListMovie) );
    }

    @Override
    public void onClickItem(int index) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DATA_IDENTIFIER, mListMovie.get(index));
        startActivity(i);
    }

    void getFavourite(){
        isFavourite = true;
        if (mListMovie != null){
            mListMovie.clear();
        }
        mListMovie = getAllFavoriteData();
        movieListAdapter.setMovieData(mListMovie);
    }

    private void requestData(int page){
        URL movieURL = null;
        if (isSortByPopular){
            movieURL = NetworkUtils.buildUrl(NetworkUtils.SORT_POPULAR,page);
        }
        else {
            movieURL = NetworkUtils.buildUrl(NetworkUtils.SORT_TOPRATED,page);
        }
        new FetchMovie().execute(movieURL);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }



    List<Movie> getAllFavoriteData(){
        Cursor cursor = movieContentProvider.query(null,null,null,null,null);

        List<Movie> moviews = new ArrayList<>();
        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_TITLE));
            String imgPoster = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_IMGPOSTER));
            String overView = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_OVERVIEW));
            String vote_average = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_VOTE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieReaderContract.MovieEntry.COLUMN_NAME_RELEASEDATE));

            Movie movie = new Movie(Integer.valueOf(id), title, imgPoster, overView, Double.valueOf(vote_average), releaseDate);

            moviews.add(movie);
        }

        if (moviews.size() > 0){
            cursor.close();
            return moviews;
        }else{
            cursor.close();
            return null;
        }
    }

    public class FetchMovie extends AsyncTask<URL,  Void, List<Movie>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading = true;
        }

        //
        @Override
        protected List<Movie> doInBackground(URL... urls) {

            try{
                if (isOnline()){
                    String result = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                    Log.d("parser"," "+result);
                    return MovieJsonParser.serializeJson(result);
                }
                else{
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null){
                if (mListMovie == null){
                    mListMovie = movies;
                }
                else{
                    mListMovie.addAll(movies);
                }
                movieListAdapter.setMovieData(mListMovie);
            }
            mLoading = false;
            mLoadingIndicator.setVisibility(View.INVISIBLE);

        }

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
    }


}
