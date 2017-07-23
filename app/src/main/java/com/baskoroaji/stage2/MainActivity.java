package com.baskoroaji.stage2;

import android.content.Context;
import android.content.Intent;
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

import com.baskoroaji.stage2.Adapters.MovieListAdapter;
import com.baskoroaji.stage2.Models.Movie;
import com.baskoroaji.stage2.Utils.MovieJsonParser;
import com.baskoroaji.stage2.Utils.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.ClickRecyclerListener{

    private List<Movie> mListMovie;
    private RecyclerView mRecyclerView;
    private MovieListAdapter movieListAdapter;
    private boolean mLoading = true;
    private ProgressBar mLoadingIndicator;

    public static final String DATA_IDENTIFIER = "DATA";
    private final String SORTBY_IDENTIFIER = "SORTBY";

    private boolean isSortByPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loadingMovie);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        movieListAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(movieListAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItem = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!mLoading && lastVisibleItem == totalItem - 1) {
                    requestData(totalItem);
                }
            }
        });

        requestData(0);
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
                mListMovie = null;
                requestData(0);
                break;
            case R.id.action_sortbyrates:
                //request sort by highest rate
                isSortByPopular = false;
                mListMovie = null;
                requestData(0);
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickItem(int index) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DATA_IDENTIFIER, mListMovie.get(index));
        startActivity(i);
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
