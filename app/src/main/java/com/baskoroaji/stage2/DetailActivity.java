package com.baskoroaji.stage2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baskoroaji.stage2.Models.Movie;
import com.baskoroaji.stage2.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie mData;
    private TextView mMovieTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mOverViewTextView;
    private ImageView mPosterImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movietittle_detailspage);
        mYearTextView = (TextView) findViewById(R.id.tv_year_detailpage);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating_detailpage);
        mOverViewTextView = (TextView) findViewById(R.id.tv_overview_detailpage);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_detailpage);


        Intent i = getIntent();
        if (i.hasExtra(MainActivity.DATA_IDENTIFIER)){
            mData = (Movie) i.getSerializableExtra(MainActivity.DATA_IDENTIFIER);
            mMovieTitleTextView.setText(mData.title);
            mYearTextView.setText(mData.getYear());
            mRatingTextView.setText(mData.vote_average.toString());
            String imgUrl = NetworkUtils.BASE_URL_IMAGE + mData.imgPoster;
            Picasso.with(this).load(imgUrl).into(mPosterImageView);
            mOverViewTextView.setText(mData.overView);
        }
    }
}
