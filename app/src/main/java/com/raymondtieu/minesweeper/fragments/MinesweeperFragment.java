package com.raymondtieu.minesweeper.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class MinesweeperFragment extends Fragment implements AdapterView.OnTouchListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private FieldAdapter mFieldAdapter;
    private PositionPointAdapter mPositionAdapter;
    private RecyclerView mRecyclerView;

    private OnePlayerGame game;
    private int x, y, m;

    private ImageView mFlagMode;

    private TextView mDifficulty, mTimer, mMines;
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

        // set all views
        mDifficulty = (TextView) layout.findViewById(R.id.difficulty);
        mMines = (TextView) layout.findViewById(R.id.num_mines);
        mTimer = (TextView) layout.findViewById(R.id.timer);
        mFlagMode = (ImageView) layout.findViewById(R.id.flag_mode);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.minefield);


        Bundle args = getArguments();
        x = args.getInt("xDim", 16);
        y = args.getInt("yDim", 16);
        m = args.getInt("nMines", 40);

        // calculate how large a cell should be to fit 10 per row on the screen
        cellWidth = calculateCellWidth();

        // start a new game
        startNewGame();

        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(y);

        mRecyclerView.setLayoutManager(manager);

        // set up game information in header
        configureHeader();

        setViewDimensions(layout);

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

            // number of mines adjacent to cell at x, y
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


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Point p = mPositionAdapter.positionToPoint(position);

        boolean flagBefore = game.isFlagMode();

        game.setFlagMode(true);
        game.reveal(p.x, p.y);
        game.setFlagMode(flagBefore);

        return true;
    }


    private void configureHeader() {

        switch(y) {
            case 9 : mDifficulty.setText(R.string.easy_title); break;
            case 16: mDifficulty.setText(R.string.medium_title); break;
            case 30: mDifficulty.setText(R.string.hard_title); break;
        }

        mMines.setText("" + m);

        mFlagMode.setImageResource(R.drawable.flag_deselect);
        mFlagMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!game.isFinished()) {
                    game.toggleFlag();

                    if (game.isFlagMode()) {
                        mFlagMode.setImageResource(R.drawable.flag_select);
                    } else {
                        mFlagMode.setImageResource(R.drawable.flag_deselect);
                    }
                }
            }
        });

    }

    private void startNewGame() {
        cellWidth = calculateCellWidth();

        game = new OnePlayerGame(x, y, m);

        configureAdapters();
        configureHeader();
    }

    private void configureAdapters() {

        // create adapter to convert positions to points and points to positions
        mPositionAdapter = new PositionPointAdapter(x, y);

        // create adapter to handle mine field
        mFieldAdapter = new FieldAdapter(getActivity(), game.getField(), cellWidth);
        mFieldAdapter.setPositionAdapter(mPositionAdapter);
        mFieldAdapter.setOnItemClickListener(this);
        mFieldAdapter.setOnItemLongClickListener(this);

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
