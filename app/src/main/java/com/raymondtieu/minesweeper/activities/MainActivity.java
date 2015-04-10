package com.raymondtieu.minesweeper.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.raymondtieu.minesweeper.fragments.*;

import com.raymondtieu.minesweeper.R;

public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private Button easyMode, mediumMode, hardMode;

    private final int[] EASY = {9, 9, 10};
    private final int[] MEDIUM = {16, 16, 40};
    private final int[] HARD = {16, 30, 99};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        setUpField(MEDIUM);
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
            setUpField(EASY);
        } else if (id == R.id.action_medium) {
            setUpField(MEDIUM);
        } else if (id == R.id.action_hard) {
            setUpField(HARD);
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

    public void setUpField(int[] difficulty) {
        int x = difficulty[0];
        int y = difficulty[1];
        int m = difficulty[2];

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        MinesweeperFragment f = new MinesweeperFragment();

        Bundle args = new Bundle();

        args.putInt("xDim", x);
        args.putInt("yDim", y);
        args.putInt("nMines", m);

        f.setArguments(args);

        ft.replace(R.id.fragment_minesweeper, f);
        ft.commit();
    }
}
