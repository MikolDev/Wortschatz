package com.example.wortschatz.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wortschatz.Controller.PhraseAdapter;
import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.MainActivity;
import com.example.wortschatz.R;

public class ListFragment extends Fragment {
    MainActivity mainActivity;
    View view;
    DbHelper dbHelper;
    RecyclerView recyclerView;
    String chapter;

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

        recyclerView = view.findViewById(R.id.recyclerViewList);
        PhraseAdapter phraseAdapter = new PhraseAdapter(mainActivity, dbHelper.getPhrasesByChapter(chapter));
        recyclerView.setAdapter(phraseAdapter);

        return view;
    }
}
