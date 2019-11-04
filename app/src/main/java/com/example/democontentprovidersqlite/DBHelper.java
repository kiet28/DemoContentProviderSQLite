package com.example.democontentprovidersqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "booksdb";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Author(id_author integer primary key, name text," +
                "address text, email text)");
        db.execSQL("create table Book(id_book integer primary key, title text, id_author " +
                "integer constraint id_author references Author(id_author) on delete cascade " +
                "on update cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Author");
        db.execSQL("drop table if exists Book");
    }
}
