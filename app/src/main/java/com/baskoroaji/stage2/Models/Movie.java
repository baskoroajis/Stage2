package com.baskoroaji.stage2.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Macpro on 6/30/17.
 */
@SuppressWarnings("serial")
public class Movie implements Serializable {
    public int id;
    public String title;
    public String imgPoster;
    public String overView;
    public Double vote_average;
    public Date releaseDate;

    public Movie(int id, String title, String imgPoster, String overView, Double vote_average, String releaseDate){
        this.id = id;
        this.title = title;
        this.imgPoster = imgPoster;
        this.overView = overView;
        this.vote_average = vote_average;
        this.releaseDate = makeDateYYYYMMdd(releaseDate);
    }

    public String getYear(){
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy");
        return  dformat.format(this.releaseDate);
    }

    public String GetYYYMMDD(){
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        return  dformat.format(this.releaseDate);
    }

    private  Date makeDateYYYYMMdd(String date) {
        Date mydate = null;
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            mydate = dformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  mydate;
    }
}
