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

    public PhraseUpdater(DbHelper dbHelper, Context context) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.dataSource = dbHelper.dataSource;
    }

}
