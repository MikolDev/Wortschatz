package com.example.wortschatz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.Database.PhraseUpdater;
import com.example.wortschatz.Fragments.ChaptersFragment;
import com.example.wortschatz.Fragments.ListFragment;
import com.example.wortschatz.Model.Phrase;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public BottomAppBar bottomAppBar;
    public BottomNavigationView bottomNavigationView;
    public String currentChapter;
    public static final int CHAPTERS_FRAGMENT_ID = 0;
    public static final int LIST_FRAGMENT_ID = 1;
    public DbHelper dbHelper;
    public PhraseUpdater phraseUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView.setSelectedItemId(R.id.item_chapters);

        changeFragment(CHAPTERS_FRAGMENT_ID);
        initNavigationListener();

        dbHelper = new DbHelper(this);
        phraseUpdater = new PhraseUpdater(dbHelper, this);
    }

    public void changeFragment(int id) {
        Fragment fragment;

        switch(id) {
            case CHAPTERS_FRAGMENT_ID:
                fragment = new ChaptersFragment();
                break;
            case LIST_FRAGMENT_ID:
                fragment = new ListFragment();
                break;
            default:
                fragment = new ChaptersFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void initNavigationListener() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chapters:
                        changeFragment(CHAPTERS_FRAGMENT_ID);
                        return true;
                    case R.id.item_list:
                        changeFragment(LIST_FRAGMENT_ID);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_update_phrases:
                Log.v("update", "Updating new words from files.");
                phraseUpdater.checkAllFiles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}