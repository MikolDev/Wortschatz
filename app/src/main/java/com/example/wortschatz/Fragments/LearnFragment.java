package com.example.wortschatz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wortschatz.Controller.OnSwipeTouchListener;
import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.MainActivity;
import com.example.wortschatz.Model.Phrase;
import com.example.wortschatz.R;

import java.util.ArrayList;

public class LearnFragment extends Fragment {
    private MainActivity mainActivity;
    private View view;
    private DbHelper dbHelper;
    private ArrayList<Phrase> phrasesList;
    private String chapter;
    private TextView tvSingular;
    private TextView tvPlural;
    private TextView tvCounter;
    private int currentIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_learn, container, false);
        mainActivity = (MainActivity) getActivity();
        dbHelper = mainActivity.getDbHelper();
        chapter = mainActivity.getCurrentChapter();

        tvSingular = view.findViewById(R.id.learn_singular);
        tvPlural = view.findViewById(R.id.learn_plural);
        tvCounter = view.findViewById(R.id.learn_counter);
        phrasesList = dbHelper.getPhrasesByChapter(chapter);

        setCounter(0);
        showPhrase(0);

        initSwipeListener();

        return view;
    }

    public void setCounter(int i) {
        if (i > phrasesList.size()){
            i = 0;
        }
        i += 1;
        tvCounter.setText(i + " / " + phrasesList.size());
    }

    public void showPhrase(int i) {
        Phrase currentPhrase = phrasesList.get(i);

        tvSingular.setText(currentPhrase.getSingular());
        tvPlural.setText(currentPhrase.getPlural());
    }

    public void initSwipeListener() {
        view.setOnTouchListener(new OnSwipeTouchListener(mainActivity){
            @Override
            public void onSwipeRight() {
                if (currentIndex == 0) {
                    currentIndex = phrasesList.size() - 1;
                    setCounter(currentIndex);
                    showPhrase(currentIndex);
                } else {
                    currentIndex -= 1;
                    setCounter(currentIndex);
                    showPhrase(currentIndex);
                }
            }

            @Override
            public void onSwipeLeft() {
                if (currentIndex + 1 < phrasesList.size()) {
                    currentIndex += 1;
                    setCounter(currentIndex);
                    showPhrase(currentIndex);
                } else {
                    currentIndex = 0;
                    setCounter(currentIndex);
                    showPhrase(currentIndex);
                }
            }
        });
    }
}
