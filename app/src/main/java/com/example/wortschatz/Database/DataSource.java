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
    // mozna dac do assetow
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

    public DataSource(Context context) {
        this.context = context;
        chaptersList = createChaptersList();
        phrasesList = new ArrayList<>();
    }

    /**
     * Create ArrayList of chapters from String[].
     *
     * @return list of chapters
     */
    private ArrayList<String> createChaptersList() {
        ArrayList<String> list = new ArrayList<>();

        Collections.addAll(list, CHAPTERS);

        return list;
    }

    /**
     * Getter to read all chapters' names.
     *
     * @return list of chapters
     */
    public ArrayList<String> getChaptersList() {
        return chaptersList;
    }

}
