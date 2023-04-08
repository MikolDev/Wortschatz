package com.example.wortschatz.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wortschatz.Model.Phrase;

import java.util.ArrayList;

public class PhraseUpdater {
    public static final String TAG = "update";
    Context context;
    public DataSource dataSource;
    public DbHelper dbHelper;

    private final String[] CHAPTERS;
    private final String[] FILE_NAMES;

    public PhraseUpdater(DbHelper dbHelper, Context context) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.dataSource = dbHelper.dataSource;
        this.CHAPTERS = dataSource.getCHAPTERS();
        this.FILE_NAMES = dataSource.getFILE_NAMES();
    }

    // zamiast porównywać długości w bazie i w assets, powinienem sprawdzić, czy któryś plik w assets został zmodyfikowany
    public boolean checkFileAndDb(int chapterIndex) {
        ArrayList<Phrase> list = dataSource.getPhrasesListByChapter(chapterIndex);
        int fileLength = list.size();

        int dbLength = dbHelper.getChapterCount(CHAPTERS[chapterIndex]);
        Log.v("update", "File length = " + fileLength + " DB chapter length = " + dbLength);

        return fileLength == dbLength;
    }

    public void checkAllFiles() {
        int updatedPhrases = 0;

        for (int i = 0; i < CHAPTERS.length; i++) {
            if (!checkFileAndDb(i)) {
                updatedPhrases += dbHelper.updatePhrasesByChapter(i);
            }
        }

        Toast.makeText(context, "Zaktualizowano " + updatedPhrases + " wyrażeń", Toast.LENGTH_SHORT).show();
    }

    public int findChapterIndex(String chapter) {
        for (int i = 0; i < CHAPTERS.length; i++) {
            if (chapter.trim().equalsIgnoreCase(CHAPTERS[i].trim())) {
                return i;
            }
        }
        return -1;
    }

    public boolean addPhrase(Phrase p) {
        if (dbHelper.doPhraseExist(p)) return false;
        long response = dbHelper.insertPhrase(p);
        return response > 0;
    }
}
