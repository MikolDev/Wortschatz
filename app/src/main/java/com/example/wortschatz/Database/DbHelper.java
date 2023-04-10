package com.example.wortschatz.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.wortschatz.Model.Phrase;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 5;
    public DataSource dataSource;
    public static final String TAG = "Database";

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

        // inserting phrases from all files
        for (int i = 0; i < dataSource.getCHAPTERS().length; i++) {
            insertPhrasesFromFile(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chapters");
        db.execSQL("DROP TABLE IF EXISTS phrases");
    }

    // inserting chapters names
    private void insertChapters(SQLiteDatabase db) {
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

    // getting list of chapters names
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

    // inserting all the phrases from files
    private int insertPhrasesFromFile(SQLiteDatabase db, int chapterIndex) {
        ArrayList<ContentValues> contentValuesList = new ArrayList<>();
        ArrayList<Phrase> repo = dataSource.getPhrasesListByChapter(chapterIndex);
        int counter = 0;

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
            counter += 1;
        }

        contentValuesList.forEach((contentValues -> {
            db.insert("phrases", null, contentValues);
        }));

        return counter;
    }

    // getting all phrases from one chapter
    public ArrayList<Phrase> getPhrasesByChapter(String chapter) {
        ArrayList<Phrase> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT singular, plural, translation, isHard, chapter, id FROM phrases WHERE chapter = \"" + chapter + "\"", null);

        if (c.moveToFirst()){
            do {
                String singular = c.getString(0);
                String plural = c.getString(1);
                String translation = c.getString(2);
                boolean isHard = c.getInt(3) > 0;
                String chapterName = c.getString(4);
                int id = c.getInt(5);

                list.add(new Phrase(id, singular, plural, translation, isHard, chapterName));
                Log.v(TAG, singular);
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    // counts all phrases from chapter
    public int getChapterCount(String chapter) {
        int counter = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM phrases WHERE chapter = \"" + chapter + "\"", null);

        if (c.moveToFirst()){
            do {
                counter = c.getInt(0);
            } while (c.moveToNext());
        }

        return counter;
    }

    private long deletePhrasesByChapter(String chapter) {
        SQLiteDatabase db = getReadableDatabase();
        long success = db.delete("phrases", "chapter=?", new String[]{chapter});
        return success;
    }

    public int updatePhrasesByChapter(int chapterIndex) {
        long deletedPhrases = deletePhrasesByChapter(dataSource.getCHAPTERS()[chapterIndex]);
        SQLiteDatabase db = this.getWritableDatabase();
        int updatedPhrases = (int) (insertPhrasesFromFile(db, chapterIndex) - deletedPhrases);
        return updatedPhrases;
    }

    public boolean doPhraseExist(Phrase p) {
        SQLiteDatabase db = this.getReadableDatabase();
        String singular = p.getSingular();
        String translation = p.getTranslation();

        Cursor c = db.rawQuery("SELECT singular, translation FROM phrases WHERE singular = \"" + singular + "\" AND translation = \"" + translation + "\"", null);

        if (c.getCount() > 0) return true;
        return false;
    }

    public long insertPhrase(Phrase p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("singular", p.getSingular());
        cv.put("plural", p.getPlural());
        cv.put("translation", p.getTranslation());
        cv.put("isHard", p.isHard());
        cv.put("chapter", p.getChapter());

        long success = -1;

        if (!doPhraseExist(p)) {
            success = db.insert("phrases", null, cv);
        }

        return success;
    }

    public boolean changingOnlyHard(Phrase p1, Phrase p2) {
        return p1.getSingular().equals(p2.getSingular())
                && p1.getPlural().equals(p2.getPlural())
                && p1.getTranslation().equals(p2.getTranslation())
                && p1.isHard() != p2.isHard();
    }

    public long updatePhrase(Phrase newPhrase, Phrase oldPhrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("singular", newPhrase.getSingular());
        cv.put("plural", newPhrase.getPlural());
        cv.put("translation", newPhrase.getTranslation());
        cv.put("isHard", newPhrase.isHard());
        cv.put("chapter", newPhrase.getChapter());

        int id = newPhrase.getId();
        long success = -1;

        if (!doPhraseExist(newPhrase) || changingOnlyHard(newPhrase, oldPhrase)){
            success = db.update("phrases", cv, "id=?", new String[]{String.valueOf(id)});
        }

        return success;
    }

    public long deletePhraseById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        long success = db.delete("phrases", "id=?", new String[]{String.valueOf(id)});
        return success;
    }
}
