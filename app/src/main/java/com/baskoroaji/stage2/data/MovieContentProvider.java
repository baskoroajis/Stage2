package com.baskoroaji.stage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Macpro on 8/1/17.
 */

public class MovieContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.baskoroaji.stage2.MoviesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/movies";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private Context mContext;


    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        MovieReaderDBHelper helper = new MovieReaderDBHelper(mContext);
        SQLiteDatabase readDB = helper.getReadableDatabase();

        Cursor cursor = readDB.query(
                MovieReaderContract.MovieEntry.TABLE_NAME,                     // The table to query
                null,                               // The columns to return
                s,                                // The columns for the WHERE clause
                strings1,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        MovieReaderDBHelper helper = new MovieReaderDBHelper(mContext);
        SQLiteDatabase writeDB = helper.getReadableDatabase();
        long rowID = writeDB.insert(MovieReaderContract.MovieEntry.TABLE_NAME, null, contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            mContext.getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        MovieReaderDBHelper mDbHelper = new MovieReaderDBHelper(mContext);
        SQLiteDatabase writeDB = mDbHelper.getWritableDatabase();
        writeDB.delete(MovieReaderContract.MovieEntry.TABLE_NAME, s, strings);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
