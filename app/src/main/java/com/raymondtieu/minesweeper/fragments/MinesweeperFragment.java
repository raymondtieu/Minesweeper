package com.raymondtieu.minesweeper.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;

import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.layouts.FixedGridLayoutManager;

public class MinesweeperFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FieldAdapter mFieldAdapter;
    private PositionPointAdapter mPositionAdapter;
    private RecyclerView mRecyclerView;

    private OnePlayerGame game;
    private int x, y, m;

    private ImageView mFlagMode;

    private TextView Difficulty, Time, Mines;
    private int cellWidth;

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

        // calculate how large a cell should be to fit 10 per row on the screen
        cellWidth = calculateCellWidth();

        // instantiate a recycler view to display game
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.minefield);

        // start a new game
        startNewGame();

        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(y);

        mRecyclerView.setLayoutManager(manager);

        // set up game information in footer
        Difficulty = (TextView) layout.findViewById(R.id.difficulty);

        switch(y) {
            case 9 : Difficulty.setText("Easy"); break;
            case 16: Difficulty.setText("Medium"); break;
            case 30: Difficulty.setText("Hard"); break;
        }

        // set up game info

        Mines = (TextView) layout.findViewById(R.id.num_mines);
        Mines.setText("" + m);

        setViewDimensions(layout);

        setFlagMode(layout);

        return layout;
    }

    // calculate the size of a cell based on screen dpi
    // try to fit 10 on the screen
    private int calculateCellWidth() {
        DisplayMetrics display = getActivity().getResources().getDisplayMetrics();
        return Math.round(display.widthPixels / ((float) 10.5));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Point p = mPositionAdapter.positionToPoint(position);

        if (!game.isStarted()) {
            game.startGame(p.x, p.y);
        } else if (!game.isFinished()) {


            int result = game.reveal(p.x, p.y);

            if (result >= 9) {
                game.revealAllMines();

                new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.lost_title)
                    .setMessage(R.string.lost_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int button) {
                                startNewGame();
                            }
                        })
                    .setNegativeButton(android.R.string.no, null).show();
            } else if (result == -1) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.win_title)
                        .setMessage(R.string.win_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int button) {
                                startNewGame();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }
    }

    private void startNewGame() {
        cellWidth = calculateCellWidth();

        game = new OnePlayerGame(x, y, m);

        configureAdapters();
    }

    private void configureAdapters() {

        // create adapter to convert positions to points and points to positions
        mPositionAdapter = new PositionPointAdapter(x, y);

        // create adapter to handle mine field
        mFieldAdapter = new FieldAdapter(getActivity(), game.getField(), cellWidth);
        mFieldAdapter.setPositionAdapter(mPositionAdapter);
        mFieldAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mFieldAdapter);

        /// set adapter in the game to notify view for changes
        game.setFieldAdapter(mFieldAdapter);
        game.setPositionAdapter(mPositionAdapter);

        mFieldAdapter.notifyDataSetChanged();
    }

    private void setViewDimensions(View layout) {
        // set appropriate size for view containers
        mRecyclerView.getLayoutParams().height = x * cellWidth;
        mRecyclerView.getLayoutParams().width = y * cellWidth;

        FrameLayout frameLayout = (FrameLayout) layout.findViewById(R.id.minefield_container);
        frameLayout.getLayoutParams().height = x * cellWidth;
        frameLayout.getLayoutParams().width = y * cellWidth;
    }

    private void setFlagMode(View layout) {
        mFlagMode = (ImageView) layout.findViewById(R.id.flag_mode);
        mFlagMode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                game.toggleFlag();
            }
        });
    }

}
