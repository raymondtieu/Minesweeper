package com.raymondtieu.minesweeper.fragments;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.raymondtieu.minesweeper.R;

import com.raymondtieu.minesweeper.adapters.CellAdapter;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

public class MinesweeperFragment extends Fragment {

    private GridView gridView;
    private CellAdapter adapter;

    private OnePlayerGame game;
    private int x, y, m;

    public MinesweeperFragment() {
        // Required empty public constructor
    }

    public static MinesweeperFragment newInstance(int x, int y, int m) {
        Bundle args = new Bundle();

        args.putInt("xDim", x);
        args.putInt("yDim", y);
        args.putInt("nMines", m);

        MinesweeperFragment f = new MinesweeperFragment();

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layout = inflater
            .inflate(R.layout.fragment_minesweeper, container, false);

        Bundle args = getArguments();
        x = args.getInt("xDim", 16);
        y = args.getInt("yDim", 16);
        m = args.getInt("nMines", 40);

        game = new OnePlayerGame(x, y, m);

        gridView = (GridView) layout.findViewById(R.id.minesweeper_board);
        gridView.setNumColumns(y);

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
}
