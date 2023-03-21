package com.example.wortschatz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private DataSource dataSource;

    public DbHelper(@Nullable Context context) {
        super(context, "WortschatzDB.db", null, VERSION);
        dataSource = new DataSource();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHAPTERS_TABLE = "CREATE TABLE chapters ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT"
                + ");";
        db.execSQL(CREATE_CHAPTERS_TABLE);
        insertChapters(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chapters");
    }

    public void insertChapters(SQLiteDatabase db) {
        ArrayList<ContentValues> contentValuesList = new ArrayList<>();
        ArrayList<String> repo = dataSource.getChaptersList();

        for (int i = 0; i < repo.size(); i++) {
            ContentValues cv = new ContentValues();
            String chapter = repo.get(i);

            cv.put("title", chapter);

            contentValuesList.add(cv);
        }

        contentValuesList.forEach((contentValues -> {
            db.insert("chapters", null, contentValues);
        }));
    }

    public ArrayList<String> getChapters() {
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT title FROM chapters", null);

        if (c.moveToFirst()){
            do {
                list.add(c.getString(0));
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }
}
