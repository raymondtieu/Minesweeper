package com.raymondtieu.minesweeper.views.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.views.fragments.HeaderFragment;
import com.raymondtieu.minesweeper.views.fragments.MineFieldFragment;
import com.raymondtieu.minesweeper.views.fragments.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MAINACTIVITY";

    private static final String MF_FRAGMENT = "minefield_fragment";
    private static final String HEADER_FRAGMENT = "header_fragment";

    private static final String KEY_STARTED = "started";
    private static final String KEY_FINISHED = "finished";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_MINESWEEPER = "minesweeper";
    private static final String KEY_TIME = "time";

    private SharedPreferences sharedPreferences;

    private Toolbar toolbar;

    private MineFieldFragment mineFieldFragment;
    private HeaderFragment headerFragment;

    private OnePlayerGame minesweeper;

    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.i(TAG, "On Create");

        setUpToolbar();

        sharedPreferences = getSharedPreferences(GameUtils.PREF_FILE, MODE_PRIVATE);
        difficulty = sharedPreferences.getString(KEY_DIFFICULTY, GameUtils.INTERMEDIATE);

        if (savedInstanceState == null)
            setUpMinesweeper(difficulty, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_easy) {
            promptNewGame(GameUtils.BEGINNER);
        } else if (id == R.id.action_medium) {
            promptNewGame(GameUtils.INTERMEDIATE);
        } else if (id == R.id.action_hard) {
            promptNewGame(GameUtils.ADVANCED);;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment;
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }


    public void setUpMinesweeper(String difficulty, boolean newGame) {

        // check for previous unfinished game first
        boolean started = sharedPreferences.getBoolean(KEY_STARTED, false);
        boolean finished = sharedPreferences.getBoolean(KEY_FINISHED, false);

        // load previous game
        if (started && !finished && minesweeper == null) {
            Log.i(TAG, "Loading a saved game with difficulty: " + difficulty);

            Gson gson = new Gson();
            Long time = sharedPreferences.getLong(KEY_TIME, 0L);
            String jsonField = sharedPreferences.getString(KEY_MINESWEEPER, null);

            Field field = gson.fromJson(jsonField, Field.class);
            minesweeper = OnePlayerGame.getInstance(field, difficulty);

            setUpFragments(time);

        // start a new game
        } else {
            Log.i(TAG, "Starting a new game: " + difficulty);

            GameUtils gameUtils = new GameUtils(difficulty);

            minesweeper = OnePlayerGame.getInstance(gameUtils, newGame);

            setUpFragments(0L);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            // put the difficulty of last loaded fragment into preferences
            editor.putString(KEY_DIFFICULTY, gameUtils.getDifficulty());
            editor.apply();
        }

    }


    public void setUpFragments(Long time) {

        Log.i(TAG, "Setting up fragments");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle args = new Bundle();

        args.putLong(KEY_TIME, time);

        headerFragment = new HeaderFragment();
        headerFragment.setArguments(args);
        ft.replace(R.id.fragment_header, headerFragment, HEADER_FRAGMENT);

        mineFieldFragment = new MineFieldFragment();
        ft.replace(R.id.fragment_minefield, mineFieldFragment, MF_FRAGMENT);

        ft.commit();
    }

    public void promptNewGame(final String difficulty) {
        minesweeper = OnePlayerGame.getInstance();

        if (minesweeper.isStarted() && !minesweeper.isFinished()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.quit_title)
                    .setMessage(R.string.quit_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            setUpMinesweeper(difficulty, true);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            setUpMinesweeper(difficulty, true);
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    minesweeper.toggleFlag();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    minesweeper.toggleFlag();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG , "Saving");

        // save fragment
        getSupportFragmentManager().putFragment(outState, MF_FRAGMENT, mineFieldFragment);
        getSupportFragmentManager().putFragment(outState, HEADER_FRAGMENT, headerFragment);

        // save game
        outState.putParcelable(KEY_MINESWEEPER, minesweeper);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG , "Restoring");

        if (savedInstanceState != null) {

            Log.i(TAG, "Restoring fragments and game");
            mineFieldFragment = (MineFieldFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, MF_FRAGMENT);

            headerFragment = (HeaderFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, HEADER_FRAGMENT);

            minesweeper = savedInstanceState.getParcelable(KEY_MINESWEEPER);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(TAG, "Stopping");

        // put the number of mines in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_STARTED, minesweeper.isStarted());
        editor.putBoolean(KEY_FINISHED, minesweeper.isFinished());

        if (minesweeper.isStarted() && !minesweeper.isFinished()) {
            Log.i(TAG, "Saving game to preferences");

            Gson gson = new Gson();
            String jsonField = gson.toJson(minesweeper.getField());
            Long time = headerFragment.getCurrentTime();

            Log.i(TAG, "JSON FIELD: " + jsonField);
            Log.i(TAG, "TIME: " + time);

            editor.putString(KEY_MINESWEEPER, jsonField);
            editor.putLong(KEY_TIME, time);
        } else {
            editor.remove(KEY_MINESWEEPER);
            editor.remove(KEY_TIME);
        }

        editor.commit();
    }


    public void newGameHeader() {
        headerFragment.newGame();

        minesweeper = OnePlayerGame.getInstance();
    }
}
