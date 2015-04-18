package com.raymondtieu.minesweeper.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.raymondtieu.minesweeper.controllers.FieldController;
import com.raymondtieu.minesweeper.controllers.FlagController;
import com.raymondtieu.minesweeper.controllers.GameController;
import com.raymondtieu.minesweeper.controllers.MinesController;
import com.raymondtieu.minesweeper.controllers.TimerController;
import com.raymondtieu.minesweeper.layouts.FlagImageView;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.layouts.TimerImageView;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

public class MinesweeperFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private FieldAdapter mFieldAdapter;
    private PositionPointAdapter mPositionAdapter;
    private RecyclerView mRecyclerView;
    private FrameLayout mMineField;

    private OnePlayerGame game;
    private int x, y, m;

    private ImageView minesIcon;

    private FlagImageView mFlag;
    private TimerImageView timerIcon;

    private TextView mDifficulty, mTimer;
    private MinesTextView mMines;
    private int cellWidth;

    private FieldController fieldCtrl;
    private TimerController timerCtrl;
    private MinesController minesCtrl;
    private FlagController flagCtrl;
    private GameController gameCtrl;

    private static final String SAVED_GAME = "saved_game";
    private static final String SAVED_TIME = "saved_time";

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

        // get arguments
        Bundle args = getArguments();
        x = args.getInt("xDim", 16);
        y = args.getInt("yDim", 16);
        m = args.getInt("nMines", 40);

        // set all views
        mDifficulty = (TextView) layout.findViewById(R.id.difficulty);
        mMines = (MinesTextView) layout.findViewById(R.id.num_mines);
        minesIcon = (ImageView) layout.findViewById(R.id.num_mines_icon);
        mTimer = (TextView) layout.findViewById(R.id.timer);
        timerIcon = (TimerImageView) layout.findViewById(R.id.timer_icon);
        mFlag = (FlagImageView) layout.findViewById(R.id.flag_mode);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.minefield);
        mMineField = (FrameLayout) layout.findViewById(R.id.minefield_container);

        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);

        gameCtrl = new GameController(mDifficulty);
        fieldCtrl = new FieldController(mRecyclerView, mMineField);
        timerCtrl = new TimerController(timerIcon, mTimer);
        minesCtrl = new MinesController(minesIcon, mMines, getActivity());
        flagCtrl = new FlagController(mFlag);

        if (savedInstanceState == null) {
            Log.i("Fragment", "Starting a new game");
            // start a new game
            loadNewGame(x, y, m, null);
        } else {
            Log.i("Fragment", "Loading a saved game");
            // load a previous instance of the game
            timerCtrl.setUpdatedTime(savedInstanceState.getLong(SAVED_TIME));
            game = savedInstanceState.getParcelable(SAVED_GAME);
            loadNewGame(game.getField().getDimX(),
                    game.getField().getDimY(),
                    game.getField().getMines(),
                    game);
        }
    }

    public void loadNewGame(int x, int y, int m, OnePlayerGame prevGame) {
        if (prevGame == null) {
            prevGame = new OnePlayerGame(x, y, m);
            timerCtrl.setUpdatedTime(0L);
        }

        this.game = prevGame;

        // calculate how large a cell should be to fit 10 per row on the screen
        cellWidth = calculateCellWidth();

        gameCtrl.setGame(game);

        initFieldAdapter();
        setGameAdapters();

        fieldCtrl.setGameParams(cellWidth, x, y);
        fieldCtrl.initViewSize(x, y);

        minesCtrl.setGame(game);
        flagCtrl.setGame(game);
    }

    // calculate the size of a cell based on screen dpi
    // try to fit 10 on the screen
    private int calculateCellWidth() {
        DisplayMetrics display = getActivity().getResources().getDisplayMetrics();
        return Math.round(display.widthPixels / ((float) 10.5));
    }

    private void initFieldAdapter() {
        // create adapter to convert positions to points and points to positions
        mPositionAdapter = new PositionPointAdapter(x, y);

        // create adapter to handle mine field
        mFieldAdapter = new FieldAdapter(getActivity(), game.getField(), cellWidth);
        mFieldAdapter.setPositionAdapter(mPositionAdapter);
        mFieldAdapter.setOnItemClickListener(this);
        mFieldAdapter.setOnItemLongClickListener(this);

        mRecyclerView.swapAdapter(mFieldAdapter, true);

        mFieldAdapter.notifyDataSetChanged();
    }

    private void setGameAdapters() {
        gameCtrl.setFieldAdapter(mFieldAdapter);
        gameCtrl.setPositionAdapter(mPositionAdapter);
    }


    public FlagController getFlagCtrl() {
        return flagCtrl;
    }

    public GameController getGameCtrl() {
        return gameCtrl;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean startedBeforeClick = gameCtrl.isGameStarted();

        // number of mines adjacent to cell at x, y
        Game.Status result = gameCtrl.onClick(position);

        // start timer if game has just started
        if (gameCtrl.isGameStarted() && !startedBeforeClick)
            timerCtrl.start();

        handleGameOver(result);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (game.isFinished())
            return false;

        return gameCtrl.onLongClick(position);
    }


    public void handleGameOver(Game.Status result) {
        if (result == Game.Status.LOSE) {
            game.revealAllMines();

            timerCtrl.stop();

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.lost_title)
                    .setMessage(R.string.lost_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            loadNewGame(x, y, m, null);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        } else if (result == Game.Status.WIN) {
            timerCtrl.stop();

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.win_title)
                    .setMessage(R.string.win_message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            loadNewGame(x, y, m, null);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i("Fragment", "Saving instance state");

        // save the game if it has been started
        if (game.isStarted()) {
            outState.putLong(SAVED_TIME, timerCtrl.getUpdatedTime());
            outState.putParcelable(SAVED_GAME, game);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Fragment", "Pausing..");

        if (game.isStarted() && !game.isFinished())
            timerCtrl.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("Fragment", "Resuming..");
        if (game.isStarted() && !game.isFinished())
            timerCtrl.start();
    }
}
