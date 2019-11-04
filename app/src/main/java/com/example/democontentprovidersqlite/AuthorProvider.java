package com.example.democontentprovidersqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class AuthorProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.democontentprovidersqlite.AuthorProvider";
    static final String TABLE_NAME = "Author";
    static final String CONTENT_PATH = "booksdb";
    static final String URL = "content://" + AUTHORITY + "/" + CONTENT_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);
    private SQLiteDatabase db;
    private static HashMap<String, String> AUTHOR_PROJECTION_MAP;

    static final int ALLITEMS = 1;
    static final int ONEITEM = 2;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ALLITEMS);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", ONEITEM);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        if (db != null) return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables("Author");
        switch(uriMatcher.match(uri)) {
            case ALLITEMS: {
                sqLiteQueryBuilder.setProjectionMap(AUTHOR_PROJECTION_MAP);
                break;
            }
            case ONEITEM: {
                sqLiteQueryBuilder.appendWhere("id_author="+uri.getPathSegments().get(1));
                break;
            }
        }
        if(sortOrder == "" || sortOrder == null) {
            sortOrder = "name";
        }
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return getContext().getContentResolver().getType(Settings.System.CONTENT_URI);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long row = db.insert(TABLE_NAME, null, contentValues);
        if(row > 0) {
            Uri uriReturn = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(uriReturn, null);
            return uriReturn;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = db.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
