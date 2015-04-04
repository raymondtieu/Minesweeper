package com.raymondtieu.minesweeper.fragments;


import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;


public class GameInfoFragment extends Fragment {

    boolean gameStart = false;
    // timer;

    TextView xDimensions, yDimensions, Mines, Timer;

    public GameInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_info, container, false);
    }

    public void setUp(int x, int y, int m) {
        xDimensions = (TextView) getActivity().findViewById(R.id.x_size);
        xDimensions.setText("" + x);

        yDimensions = (TextView) getActivity().findViewById(R.id.y_size);
        yDimensions.setText("" + y);

        Mines = (TextView) getActivity().findViewById(R.id.num_mines);
        Mines.setText("" + m);
    }

}
