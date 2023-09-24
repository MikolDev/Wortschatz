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
    private static final int VERSION = 6;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chapters");
        db.execSQL("DROP TABLE IF EXISTS phrases");
    }

    /**
     * Inserts all chapters names from assets to local database.
     *
     * @param db local database
     */
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

    /**
     * Returns list of chapters from local database.
     *
     * @return chapters list
     */
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


    /**
     * Returns all phrases from given chapter from local database.
     *
     * @param chapter desired chapter
     * @return list of phrases from desired chapter
     */
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

    /**
     * Counts phrases in a given chapter.
     *
     * @param chapter desired chapter
     * @return count of phrases in a chapter
     */
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

    /**
     * Deletes all phrases from a given chapter
     *
     * @param chapter desired chapter
     * @return if deleted successfully
     */
    private long deletePhrasesByChapter(String chapter) {
        SQLiteDatabase db = getReadableDatabase();
        long success = db.delete("phrases", "chapter=?", new String[]{chapter});
        return success;
    }


    /**
     * Checks if phrase exists. Checks only singular form and translation.
     *
     * @param p phrase to check
     * @return if phrase exists
     */
    public boolean doPhraseExist(Phrase p) {
        SQLiteDatabase db = this.getReadableDatabase();
        String singular = p.getSingular();
        String translation = p.getTranslation();

        Cursor c = db.rawQuery("SELECT singular, translation FROM phrases WHERE singular = \"" + singular + "\" AND translation = \"" + translation + "\"", null);

        if (c.getCount() > 0) return true;
        return false;
    }

    /**
     * Inserts phrase to local database.
     *
     * @param p phrase to insert
     * @return if inserted successfully
     */
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

    /**
     * Updates a phrase
     *
     * @param newPhrase new phrase
     * @param oldPhrase phrase to edit
     * @return if updated successfully
     */
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

    /**
     * Deletes a phrase with given id.
     *
     * @param id id of phrase to delete
     * @return if deleted successfully
     */
    public long deletePhraseById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        long success = db.delete("phrases", "id=?", new String[]{String.valueOf(id)});
        return success;
    }
}
