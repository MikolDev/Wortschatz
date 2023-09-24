package com.example.wortschatz;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.wortschatz.Database.DbHelper;
import com.example.wortschatz.Database.PhraseUpdater;
import com.example.wortschatz.Fragments.ChaptersFragment;
import com.example.wortschatz.Fragments.LearnFragment;
import com.example.wortschatz.Fragments.ListFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public BottomAppBar bottomAppBar;
    public BottomNavigationView bottomNavigationView;
    public String currentChapter = "";
    // mozna dac do assetow
    public static final int CHAPTERS_FRAGMENT_ID = 0;
    public static final int LIST_FRAGMENT_ID = 1;
    public static final int LEARN_FRAGMENT_ID = 2;
    private DbHelper dbHelper;
    public PhraseUpdater phraseUpdater;
    public DividerItemDecoration divider;

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

        currentChapter = dbHelper.dataSource.getChaptersList().get(0);

        divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
    }

    /**
     * Changes view by given id.
     *
     * @param id id of desired view
     */
    public void changeFragment(int id) {
        Fragment fragment = null;

        switch(id) {
            case CHAPTERS_FRAGMENT_ID:
                fragment = new ChaptersFragment();
                break;
            case LIST_FRAGMENT_ID:
                fragment = new ListFragment();
                break;
            case LEARN_FRAGMENT_ID:
                fragment = new LearnFragment();
                break;
            default:
                fragment = new ChaptersFragment();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack("tag").commit();
    }

    /**
     * Starts listener on navigation.
     *
     */
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
                    case R.id.item_learn:
                        changeFragment(LEARN_FRAGMENT_ID);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * Creates top menu options.
     *
     * @param menu top menu
     * @return menu options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Add phrases from .txt file
            case R.id.item_add_phrases_from_txt:
                Toast.makeText(this, "Funkcjonalność w budowie", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Getter for current chapter name.
     *
     * @return choosed chapter name
     */
    public String getCurrentChapter() {
        return currentChapter;
    }

    /**
     * Getter for local database helper.
     *
     * @return local database helper
     */
    public DbHelper getDbHelper() {
        return dbHelper;
    }
}