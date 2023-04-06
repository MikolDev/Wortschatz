package com.example.wortschatz.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wortschatz.Controller.PhraseAdapter;
import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.Database.PhraseUpdater;
import com.example.wortschatz.MainActivity;
import com.example.wortschatz.Model.Phrase;
import com.example.wortschatz.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    MainActivity mainActivity;
    View view;
    DbHelper dbHelper;
    RecyclerView recyclerView;
    String chapter;
    PhraseUpdater phraseUpdater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_list, container, false);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            dbHelper = mainActivity.dbHelper;
        } else {
            Log.v("tescior", "Main activity null error");
        }
        chapter = mainActivity.currentChapter;
        phraseUpdater = mainActivity.phraseUpdater;

        recyclerView = view.findViewById(R.id.recyclerViewList);
        ArrayList<Phrase> phrasesList = dbHelper.getPhrasesByChapter(chapter);
        PhraseAdapter phraseAdapter = new PhraseAdapter(mainActivity, phrasesList);
        recyclerView.setAdapter(phraseAdapter);

        TextView titleView = view.findViewById(R.id.chapter_title);
        titleView.setText(chapter);

        initAddListener();

        return view;
    }

    public void initAddListener() {
        FloatingActionButton addButton = view.findViewById(R.id.add_floating_button);
        addButton.setOnClickListener(v -> {
            showAlertDialog();
        });
    }

    public void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle(getString(R.string.add_phrase));

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_add_phrase, null);

        alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editSingular = dialogLayout.findViewById(R.id.edit_add_singular);
                EditText editPlural = dialogLayout.findViewById(R.id.edit_add_plural);
                EditText editTranslation = dialogLayout.findViewById(R.id.edit_add_translation);

                String singular = String.valueOf(editSingular.getText());
                String plural = String.valueOf(editPlural.getText());
                String translation = String.valueOf(editTranslation.getText());

                // liczba mnoga może nie istnieć
                if (!singular.equals("") && !translation.equals("")) {
                    Phrase phrase = new Phrase(singular, plural, translation, chapter);
                    Snackbar.make(view, "Nowe słówko " + phrase, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                dialog.dismiss();
            }
        });

        alert.setView(dialogLayout);
        alert.show();

    }
}
