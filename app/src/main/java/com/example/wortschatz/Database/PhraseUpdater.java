package com.example.wortschatz.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.wortschatz.Model.Phrase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public ArrayList<Phrase> getPhrasesByChapter(int chapterIndex) {
        String str = "";
        String chapter = CHAPTERS[chapterIndex];
        String fileName = FILE_NAMES[chapterIndex];
        ArrayList<Phrase> list = new ArrayList<>();

        try {
            // tu trzeba zmienić assets na plik z com.example
            // albo wszystko się sypie, bo nie da sie nic wpisac do assets
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (inputStream != null) {
                while ((str = bufferedReader.readLine()) != null) {
                    String[] array = str.split(";");
                    String singular = array[0].trim();
                    String plural = array[1].trim();
                    String translation = array[2].trim();
                    int isHard = Integer.parseInt(array[3].trim());

                    Phrase phrase = new Phrase();
                    phrase.setSingular(singular);
                    phrase.setPlural(plural);
                    phrase.setTranslation(translation);
                    phrase.setHard(isHard == 1);
                    phrase.setChapter(chapter);

                    list.add(phrase);
                }
            }

            if (inputStream != null) {
                inputStream.close();
            }

        } catch (IOException e) {
            Log.v(TAG, "Nie znaleziono pliku");
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkFileAndDb(int chapterIndex) {
        ArrayList<Phrase> list = getPhrasesByChapter(chapterIndex);
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
