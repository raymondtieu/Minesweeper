package com.raymondtieu.minesweeper.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;

import com.raymondtieu.minesweeper.fragments.*;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.services.Game;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Button easyMode, mediumMode, hardMode;

    private final int[] BEGINNER = {9, 9, 10};
    private final int[] INTERMEDIATE = {16, 16, 40};
    private final int[] ADVANCED = {16, 30, 99};

    private MinesweeperFragment minesweeperFragment;
    private static final String MS_FRAGMENT = "minesweeper_fragment";

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        setUpField(INTERMEDIATE, Game.Difficulty.INTERMEDIATE);
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
            setUpField(BEGINNER, Game.Difficulty.BEGINNER);
        } else if (id == R.id.action_medium) {
            setUpField(INTERMEDIATE, Game.Difficulty.INTERMEDIATE);
        } else if (id == R.id.action_hard) {
            setUpField(ADVANCED, Game.Difficulty.ADVANCED);
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

    public void setUpField(final int[] mode, final Game.Difficulty difficulty) {
        FragmentManager fm = getSupportFragmentManager();

        minesweeperFragment = (MinesweeperFragment) fm.findFragmentByTag(MS_FRAGMENT);

        if (savedInstanceState == null && minesweeperFragment == null) {
            Log.i("MainActivity", "no saved state or fragment");

            replaceFieldFragment(mode, difficulty);

        } else if (savedInstanceState != null) {

            Log.i("MainActivity", "Saved instance state");

        } else if (minesweeperFragment != null) {
            Log.i("MainActivity", "Saved fragment");

            boolean started = minesweeperFragment.getGameCtrl().isGameStarted();
            boolean finished = minesweeperFragment.getGameCtrl().isGameFinished();

            // prompt to quit unfinished game
            if (started && !finished) {
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
        args.putInt("xDim", x);
        args.putInt("yDim", y);
        args.putInt("nMines", m);
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

        Log.i("MainActivity" , "Saving");

        getSupportFragmentManager().putFragment(outState, MS_FRAGMENT, minesweeperFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.i("MainActivity" , "Restoring");

        if (savedInstanceState != null) {
            minesweeperFragment = (MinesweeperFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, MS_FRAGMENT);
        }
    }
}
