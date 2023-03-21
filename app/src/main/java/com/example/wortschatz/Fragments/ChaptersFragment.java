package com.example.wortschatz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.MainActivity;
import com.example.wortschatz.R;

import java.util.ArrayList;
import java.util.List;

public class ChaptersFragment extends Fragment {
    MainActivity mainActivity;
    View view;
    DbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_chapters, container, false);
        mainActivity = (MainActivity) getActivity();
        dbHelper = new DbHelper(mainActivity);

        ListView listView = view.findViewById(R.id.list_view_chapters);
        ArrayList<String> chapters = dbHelper.getChapters();
        ArrayAdapter<String> chaptersAdapter = new ArrayAdapter<String>(mainActivity, R.layout.item_chapter, chapters);
        listView.setAdapter(chaptersAdapter);


        return view;
    }
}
