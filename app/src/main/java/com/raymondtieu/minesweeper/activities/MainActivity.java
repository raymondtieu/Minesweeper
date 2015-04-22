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

public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private final static String PREF_FILE = "minesweeper_pref";
    private final static String MS_FRAGMENT = "minesweeper_fragment";

    private final static String KEY_STARTED = "started";
    private final static String KEY_FINISHED = "finished";
    private final static String KEY_DIFFICULTY = "difficulty";
    private final static String KEY_MINESWEEPER = "minesweeper";
    private final static String KEY_TIME = "time";

    private final static String KEY_MINESWEEPER_BUNDLE = "minesweeper_bundle";

    private SharedPreferences sharedPreferences;

    private Toolbar toolbar;

    private MinesweeperFragment minesweeperFragment;

    private OnePlayerGame minesweeper;
    private Long gameTime = 0L;
    private Game.Difficulty difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.i(TAG, "OnCreate");

        setUpToolbar();
        sharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        if (savedInstanceState == null) {
            Log.i(TAG, "No saved instance, creating new fragment");

            setUpMinesweeper();

        } else {
            Log.i(TAG, "Saved instance state found");
        }
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
            difficulty = Game.Difficulty.BEGINNER;
            setUpMinesweeper();
        } else if (id == R.id.action_medium) {
            difficulty = Game.Difficulty.INTERMEDIATE;
            setUpMinesweeper();
        } else if (id == R.id.action_hard) {
            difficulty = Game.Difficulty.ADVANCED;
            setUpMinesweeper();
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


    public void setUpMinesweeper() {

        boolean started = sharedPreferences.getBoolean(KEY_STARTED, false);
        boolean finished = sharedPreferences.getBoolean(KEY_FINISHED, false);

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

        } else {
            Log.i(TAG, "Creating a new game");

            // load a new game
            // medium by default
            if (difficulty == null) {
                int x = sharedPreferences.getInt(KEY_DIFFICULTY, 1);
                difficulty = Game.Difficulty.values()[x];
            }

            int[] diff;

            if (difficulty == Game.Difficulty.BEGINNER)
                diff = getResources().getIntArray(R.array.beginner);
            else if (difficulty == Game.Difficulty.INTERMEDIATE)
                diff = getResources().getIntArray(R.array.intermediate);
            else
                diff = getResources().getIntArray(R.array.advanced);

            minesweeper = new OnePlayerGame(diff[0], diff[1], diff[2]);


            SharedPreferences.Editor editor = sharedPreferences.edit();

            // reset the started and finished keys in preferences
            editor.putBoolean(KEY_STARTED, false);
            editor.putBoolean(KEY_FINISHED, false);
            editor.commit();
        }

        setUpFragment();
    }

    public void setUpFragment() {

        if (minesweeperFragment == null) {
            Log.i(TAG, "No saved fragment, creating new one");

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

        } else {
            Log.i(TAG, "Saved fragment");
        }
    }

    public void setUpField(final int[] mode, final Game.Difficulty difficulty) {
        FragmentManager fm = getSupportFragmentManager();

        minesweeperFragment = (MinesweeperFragment) fm.findFragmentByTag(MS_FRAGMENT);

        if (minesweeperFragment == null) {
            Log.i(TAG, "no saved state or fragment");

            replaceFieldFragment(mode, difficulty);

        } else {
            Log.i(TAG, "Saved fragment");

            // prompt to quit unfinished game
            if (minesweeper.isStarted() && !minesweeper.isFinished()) {
                new AlertDialog.Builder(this)
                    .setTitle("New Game")
                    .setMessage("Are you sure you want to quit this game and start a new one?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            replaceFieldFragment(mode, difficulty);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            } else {
                replaceFieldFragment(mode, difficulty);
            }
        }
    }

    private void replaceFieldFragment(int[] mode, Game.Difficulty difficulty) {
        int x = mode[0];
        int y = mode[1];
        int m = mode[2];

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        minesweeperFragment = new MinesweeperFragment();

        Bundle args = new Bundle();

        boolean started = sharedPreferences.getBoolean(KEY_STARTED, false);
        Long time = 0L;


        if (!started) {
            Log.i(TAG, "MINESWEEPER = NEW GAME");
            minesweeper = new OnePlayerGame(x, y, m);
        } else {
            Log.i(TAG, "MINESWEEPER = READ");
            Gson gson = new Gson();
            time = sharedPreferences.getLong(KEY_TIME, 0L);
            String jsonField = sharedPreferences.getString(KEY_MINESWEEPER, null);

            Field field = gson.fromJson(jsonField, Field.class);
            minesweeper = new OnePlayerGame(field);
        }

        args.putParcelable("saved_game", minesweeper);
        args.putLong("saved_time", time);
        args.putSerializable("difficulty", difficulty);

        minesweeperFragment.setArguments(args);

        ft.replace(R.id.fragment_minesweeper, minesweeperFragment, MS_FRAGMENT);
        ft.commit();


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    minesweeperFragment.getFlagCtrl().toggleFlag();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    minesweeperFragment.getFlagCtrl().toggleFlag();
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
        getSupportFragmentManager().putFragment(outState, MS_FRAGMENT, minesweeperFragment);

        // save game
        outState.putParcelable(KEY_MINESWEEPER_BUNDLE, minesweeper);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i(TAG , "Restoring");

        if (savedInstanceState != null) {

            Log.i(TAG, "Restoring fragment and game");
            minesweeperFragment = (MinesweeperFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, MS_FRAGMENT);

            minesweeper = savedInstanceState.getParcelable(KEY_MINESWEEPER_BUNDLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "Destroying");

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
    }
}
