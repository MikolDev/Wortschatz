package com.example.wortschatz.Database;

import java.util.ArrayList;

public class DataSource {
    private ArrayList<String> chaptersList;

    public DataSource() {
        chaptersList = createChaptersList();
    }

    private ArrayList<String> createChaptersList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Człowiek");
        list.add("Miejsce zamieszkania");
        list.add("Edukacja");
        list.add("Praca");
        list.add("Życie prywatne");
        list.add("Żywienie");
        list.add("Zakupy i usługi");
        list.add("Podróżowanie i turystyka");
        list.add("Kultura");
        list.add("Sport");
        list.add("Zdrowie");
        list.add("Nauka i technika");
        list.add("Świat przyrody");
        list.add("Państwo i społeczeństwo");

        return list;
    }

    public ArrayList<String> getChaptersList() {
        return chaptersList;
    }
}
