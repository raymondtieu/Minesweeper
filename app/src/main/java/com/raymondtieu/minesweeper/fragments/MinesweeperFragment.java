package com.raymondtieu.minesweeper.fragments;


import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.raymondtieu.minesweeper.R;

import com.raymondtieu.minesweeper.adapters.CellAdapter;
import com.raymondtieu.minesweeper.models.Board;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

public class MinesweeperFragment extends Fragment {

    private GridView gridView;
    private CellAdapter adapter;

    private OnePlayerGame game;

    public MinesweeperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new OnePlayerGame(16, 16, 40);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater
            .inflate(R.layout.fragment_minesweeper, container, false);


        gridView = (GridView) layout.findViewById(R.id.minesweeper_board);
        gridView.setNumColumns(16);

        adapter = new CellAdapter(getActivity(), game.getBoard());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CellAdapter adapter = (CellAdapter) gridView.getAdapter();

                final int i = position / game.getBoard().getxDimension();
                final int j = position % game.getBoard().getyDimension();

                if (!game.isStarted()) {
                    game.startGame(i, j);
                    adapter.notifyDataSetChanged();
                } else {
                    if (game.revealCell(i, j)) {
                        game.revealAll();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        return layout;
    }

    public void setUp(int x, int y, int m) {

    }

}
