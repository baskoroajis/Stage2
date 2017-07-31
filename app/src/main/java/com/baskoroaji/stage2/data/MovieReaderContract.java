package com.baskoroaji.stage2.data;

import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by Macpro on 7/30/17.
 */

public final class MovieReaderContract {
    public MovieReaderContract(){}

    public static class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMGPOSTER = "imgPoster";
        public static final String COLUMN_NAME_OVERVIEW = "overView";
        public static final  String COLUMN_NAME_VOTE = "vote_average";
        public static final String COLUMN_NAME_RELEASEDATE = "releaseDate";

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.COLUMN_NAME_ID + " TEXT," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MovieEntry.COLUMN_NAME_IMGPOSTER + " TEXT," +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    MovieEntry.COLUMN_NAME_VOTE + " TEXT," +
                    MovieEntry.COLUMN_NAME_RELEASEDATE + " TEXT" +")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;



}
