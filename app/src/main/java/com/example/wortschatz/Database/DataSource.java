package com.example.wortschatz.Database;

import android.content.Context;
import android.util.Log;

import com.example.wortschatz.Model.Phrase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class DataSource {
    Context context;
    public static final String TAG = "tescior";
    private final ArrayList<String> chaptersList;
    private final ArrayList<Phrase> phrasesList;
    private final String[] CHAPTERS = {
            "Człowiek",
            "Miejsce zamieszkania",
            "Edukacja",
            "Praca",
            "Życie prywatne",
            "Żywienie",
            "Zakupy i usługi",
            "Podróżowanie i turystyka",
            "Kultura",
            "Sport",
            "Zdrowie",
            "Nauka i technika",
            "Świat przyrody",
            "Państwo i społeczeństwo"
    };
    private final String[] FILE_NAMES = {
            "czlowiek",
            "zamieszkanie",
            "edukacja",
            "praca",
            "zycie",
            "zywienie",
            "zakupy",
            "podrozowanie",
            "kultura",
            "sport",
            "zdrowie",
            "nauka",
            "przyroda",
            "panstwo"
    };

    public DataSource(Context context) {
        this.context = context;
        chaptersList = createChaptersList();
        phrasesList = new ArrayList<>();
    }

    private ArrayList<String> createChaptersList() {
        ArrayList<String> list = new ArrayList<>();

        Collections.addAll(list, CHAPTERS);

        return list;
    }

    public ArrayList<String> getChaptersList() {
        return chaptersList;
    }

    // creating phrases list from files
    public ArrayList<Phrase> getPhrasesListByChapter(int chapterIndex) {
        String str = "";
        String chapter = CHAPTERS[chapterIndex];
        String fileName = FILE_NAMES[chapterIndex];
        ArrayList<Phrase> list = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (inputStream != null) {
                while ((str = bufferedReader.readLine()) != null) {
                    String[] array = str.split(";");
                    Log.v(TAG, array[0]);
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

                    Log.v(TAG, phrase.toString());
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

    public ArrayList<Phrase> getPhrasesList() {
        for (int i = 0; i < FILE_NAMES.length; i++) {
            ArrayList<Phrase> currentList = getPhrasesListByChapter(i);
            phrasesList.addAll(currentList);
        }
        return phrasesList;
    }

    public String[] getCHAPTERS() {
        return CHAPTERS;
    }

    public String[] getFILE_NAMES() {
        return FILE_NAMES;
    }
}
