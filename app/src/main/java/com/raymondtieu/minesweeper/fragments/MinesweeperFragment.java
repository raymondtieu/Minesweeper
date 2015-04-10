package com.raymondtieu.minesweeper.fragments;


import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.raymondtieu.minesweeper.R;

import com.raymondtieu.minesweeper.adapters.CellAdapter;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.layouts.FixedGridLayoutManager;

public class MinesweeperFragment extends Fragment implements AdapterView.OnItemClickListener {

    private CellAdapter adapter;
    private RecyclerView recyclerView;

    private OnePlayerGame game;
    private int x, y, m;
    private TextView Difficulty, Time, Mines;

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

        // instantiate a new game
        game = new OnePlayerGame(x, y, m);

        // instantiate a recycler view to display game
        recyclerView = (RecyclerView) layout.findViewById(R.id.minefield);

        // create adapter
        adapter = new CellAdapter(getActivity(), game.getBoard());
        adapter.setOnItemClickListener(this);

        // set adapter in the game to notify view for changes
        game.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(y);

        recyclerView.setLayoutManager(manager);

        // set up game information in footer
        Difficulty = (TextView) layout.findViewById(R.id.difficulty);

        switch(y) {
            case 9 : Difficulty.setText("Easy"); break;
            case 16: Difficulty.setText("Medium"); break;
            case 30: Difficulty.setText("Hard"); break;
        }

        Mines = (TextView) layout.findViewById(R.id.num_mines);
        Mines.setText("" + m);

        FrameLayout frameLayout = (FrameLayout) layout.findViewById(R.id.minefield_container);

        frameLayout.getLayoutParams().height = x*99;
        frameLayout.getLayoutParams().width = y*99;


        recyclerView.getLayoutParams().height = x * 99;
        recyclerView.getLayoutParams().width = y * 99;


        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("" + position);
        CellAdapter.Coordinates c = adapter.convertPosition(position);

        if (!game.isStarted()) {
            game.startGame(c.i, c.j);

            Toast.makeText(getActivity(),
                    "Game started at " + c.i + ", " + c.j,
                    Toast.LENGTH_SHORT).show();
        } else {
            game.revealCell(c.i, c.j);
        }
    }
}
