package com.example.wortschatz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wortschatz.Model.Phrase;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private DataSource dataSource;

    public DbHelper(@Nullable Context context) {
        super(context, "WortschatzDB.db", null, VERSION);
        dataSource = new DataSource(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHAPTERS_TABLE = "CREATE TABLE chapters ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT"
                + ");";
        db.execSQL(CREATE_CHAPTERS_TABLE);
        insertChapters(db);

        String CREATE_PHRASES_TABLE = "CREATE TABLE phrases ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "singular TEXT,"
                + "plural TEXT,"
                + "translation TEXT,"
                + "isHard BOOLEAN,"
                + "chapter TEXT"
                + ");";
        db.execSQL(CREATE_PHRASES_TABLE);
        insertPhrasesFromFiles(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chapters");
        db.execSQL("DROP TABLE IF EXISTS phrases");
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

    public void insertPhrasesFromFiles(SQLiteDatabase db) {
        ArrayList<ContentValues> contentValuesList = new ArrayList<>();
        ArrayList<Phrase> repo = dataSource.getPhrasesList();

        for (int i = 0; i < repo.size(); i++) {
            ContentValues cv = new ContentValues();
            Phrase phrase = repo.get(i);

            String singular = phrase.getSingular();
            String plural = phrase.getPlural();
            String translation = phrase.getTranslation();
            Boolean isHard = phrase.isHard();
            String chapter = phrase.getChapter();

            cv.put("singular", singular);
            cv.put("plural", plural);
            cv.put("translation", translation);
            cv.put("isHard", isHard);
            cv.put("chapter", chapter);

            contentValuesList.add(cv);
        }

        contentValuesList.forEach((contentValues -> {
            db.insert("phrases", null, contentValues);
        }));
    }

    public ArrayList<Phrase> getPhrasesByChapter(String chapter) {
        ArrayList<Phrase> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT singular, plural, translation, isHard, chapter, id FROM phrases WHERE chapter = " + chapter, null);

        if (c.moveToFirst()){
            do {
                String singular = c.getString(0);
                String plural = c.getString(1);
                String translation = c.getString(2);
                boolean isHard = c.getInt(3) > 0;
                String chapterName = c.getString(4);
                int id = c.getInt(5);

                list.add(new Phrase(id, singular, plural, translation, isHard, chapter));
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }
}
