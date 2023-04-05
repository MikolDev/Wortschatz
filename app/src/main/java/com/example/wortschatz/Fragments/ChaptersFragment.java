package com.example.wortschatz.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    String chapter;
    public static final String TAG = "tescior";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.view_chapters, container, false);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            dbHelper = mainActivity.dbHelper;
        } else {
            Log.v("tescior", "Main activity null error");
        }

        ListView listView = view.findViewById(R.id.list_view_chapters);
        ArrayList<String> chapters = dbHelper.getChapters();
        ArrayAdapter<String> chaptersAdapter = new ArrayAdapter<String>(mainActivity, R.layout.item_chapter, chapters);
        listView.setAdapter(chaptersAdapter);
        startListener(listView);

        return view;
    }

    public void startListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chapter = (String) parent.getItemAtPosition(position);
                Toast.makeText(mainActivity, "Wybrano: " + chapter, Toast.LENGTH_SHORT).show();
                mainActivity.currentChapter = chapter;
                mainActivity.changeFragment(MainActivity.LIST_FRAGMENT_ID);
            }
        });
    }
}
