package com.raymondtieu.minesweeper.activities;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.raymondtieu.minesweeper.fragments.*;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.views.fragments.HeaderFragment;
import com.raymondtieu.minesweeper.views.fragments.MineFieldFragment;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MAINACTIVITY";

    private static final String MF_FRAGMENT = "minefield_fragment";
    private static final String HEADER_FRAGMENT = "header_fragment";

    private static final String KEY_STARTED = "started";
    private static final String KEY_FINISHED = "finished";
    private static final String KEY_DIFFICULTY = "difficulty";
    private static final String KEY_MINESWEEPER = "minesweeper";
    private static final String KEY_TIME = "time";

    private static final String ARG_KEY_MINESWEEPER = "arg_minesweeper";

    private SharedPreferences sharedPreferences;

    private Toolbar toolbar;

    private MineFieldFragment mineFieldFragment;
    private HeaderFragment headerFragment;

    private OnePlayerGame minesweeper;
    private Long gameTime = 0L;

    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.i(TAG, "On Create");

        setUpToolbar();

        sharedPreferences = getSharedPreferences(GameUtils.PREF_FILE, MODE_PRIVATE);
        difficulty = sharedPreferences.getString(KEY_DIFFICULTY, GameUtils.INTERMEDIATE);

        if (savedInstanceState == null) {
            Log.i(TAG, "Setting activity up");

            setUpMinesweeper(difficulty, false);
        } else {
            Log.i(TAG, "ROTATING");
        }
/*

        Log.i(TAG, "Starting new game");
        GameUtils gameUtils = new GameUtils(GameUtils.BEGINNER);
        minesweeper = OnePlayerGame.getInstance(gameUtils, true);

        Log.i(TAG, "Setting up fragments");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle args = new Bundle();

        MineFieldFragment mineFieldFragment = new MineFieldFragment();
        HeaderFragment headerFragment = new HeaderFragment();

        ft.replace(R.id.fragment_header, headerFragment);
        ft.replace(R.id.fragment_minefield, mineFieldFragment, MS_FRAGMENT);
        ft.commit();
*/



        /*
        if (savedInstanceState == null) {
            Log.i(TAG, "No saved instance, creating new fragment");

            setUpMinesweeper(null);

        } else {
            Log.i(TAG, "Saved instance state found");
        }
        */
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

        GameUtils gameUtils = new GameUtils(difficulty);

        minesweeper = OnePlayerGame.getInstance(gameUtils, newGame);

        setUpFragments();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // put the difficulty of last loaded fragment into preferences
        editor.putString(KEY_DIFFICULTY, gameUtils.getDifficulty());
        editor.apply();

    }


    public void setUpFragments() {

        Log.i(TAG, "Setting up fragments");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle args = new Bundle();

        mineFieldFragment = new MineFieldFragment();
        headerFragment = new HeaderFragment();

        ft.replace(R.id.fragment_header, headerFragment, HEADER_FRAGMENT);
        ft.replace(R.id.fragment_minefield, mineFieldFragment, MF_FRAGMENT);
        ft.commit();

    }

    public void promptNewGame(final String difficulty) {
        minesweeper = OnePlayerGame.getInstance();

        if (minesweeper.isStarted() && !minesweeper.isFinished()) {
            new AlertDialog.Builder(this)
                    .setTitle("NEW GAME")
                    .setMessage("ARE you sure?")
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

    public void setUpMinesweeper() {

        boolean started = sharedPreferences.getBoolean(KEY_STARTED, false);
        boolean finished = sharedPreferences.getBoolean(KEY_FINISHED, false);
/*
        if (minesweeper == null) {
            if (started && !finished) {
                Log.i(TAG, "Loading saved game");

                // load unfinished game
                Gson gson = new Gson();
                gameTime = sharedPreferences.getLong(KEY_TIME, 0L);
                String jsonField = sharedPreferences.getString(KEY_MINESWEEPER, null);

                Field field = gson.fromJson(jsonField, Field.class);
                minesweeper = new OnePlayerGame(field);

                int x = sharedPreferences.getInt(KEY_DIFFICULTY, 1);
                difficulty = Game.Difficulty.values()[x];

                setUpFragment();

            } else {
                Log.i(TAG, "Creating a new game");

                // load a new game
                // medium by default
                int x = sharedPreferences.getInt(KEY_DIFFICULTY, 1);
                difficulty = Game.Difficulty.values()[x];

                setUpNewGame();
            }
        } else {
            Log.i(TAG, "Starting a new game from menu");

            // check if the current game is ongoing and prompt user to quit
            if (minesweeper.isStarted() && !minesweeper.isFinished()) {
                new AlertDialog.Builder(this)
                        .setTitle("New Game")
                        .setMessage("Are you sure you want to quit this game and start a new one?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int button) {
                                // change difficulty from menu
                                difficulty = d;
                                setUpNewGame();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            } else {
                // change difficulty from menu
                difficulty = d;
                setUpNewGame();
            }
        }
        */
    }

    public void promptNewGame(int title, int message) {
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int button) {
                    setUpNewGame();
                }
            })
            .setNegativeButton(android.R.string.no, null).show();
    }

    public void setUpNewGame() {

        /*
        Log.i(TAG, "Setting up a new game");

        int[] diff;

        if (difficulty == Game.Difficulty.BEGINNER)
            diff = getResources().getIntArray(R.array.beginner);
        else if (difficulty == Game.Difficulty.INTERMEDIATE)
            diff = getResources().getIntArray(R.array.intermediate);
        else
            diff = getResources().getIntArray(R.array.advanced);

        minesweeper = new OnePlayerGame(diff[0], diff[1], diff[2]);

        gameTime = 0L;

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // reset the started and finished keys in preferences
        editor.putBoolean(KEY_STARTED, false);
        editor.putBoolean(KEY_FINISHED, false);
        editor.commit();

        setUpFragment();
        */
    }

    public void setUpFragment() {
/*
        Log.i(TAG, "Setting up fragment");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle args = new Bundle();

        minesweeperFragment = new MinesweeperFragment();

        args.putParcelable(KEY_MINESWEEPER, minesweeper);
        args.putLong(KEY_TIME, gameTime);
        args.putSerializable(KEY_DIFFICULTY, difficulty);

        minesweeperFragment.setArguments(args);

        ft.replace(R.id.fragment_minesweeper, minesweeperFragment, MS_FRAGMENT);
        ft.commit();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // put the difficulty of last loaded fragment into preferences
        editor.putInt(KEY_DIFFICULTY, difficulty.ordinal());
        editor.commit();

        */
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

            Log.i(TAG, "Restoring fragment and game");
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
/*
        Log.i(TAG, "Stopping");

        // put the number of mines in shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_STARTED, minesweeper.isStarted());
        editor.putBoolean(KEY_FINISHED, minesweeper.isFinished());

        if (minesweeper.isStarted() && !minesweeper.isFinished()) {
            Gson gson = new Gson();
            String jsonField = gson.toJson(minesweeper.getField());
            Long time = minesweeperFragment.getTimerCtrl().getUpdatedTime();

            Log.i(TAG, "JSON GAME: " + jsonField);

            editor.putString(KEY_MINESWEEPER, jsonField);
            editor.putLong(KEY_TIME, time);
        }

        editor.commit();

        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "Destroying");
    }


    public void newGameHeader() {
        headerFragment.newGame();
    }
}
