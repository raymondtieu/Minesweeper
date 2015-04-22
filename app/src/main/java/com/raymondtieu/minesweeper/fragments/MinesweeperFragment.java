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

import com.raymondtieu.minesweeper.activities.MainActivity;
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
    private Game.Difficulty difficulty;

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

    private static final String TAG = "Fragment";

    private static final String KEY_MINESWEEPER = "minesweeper";
    private static final String KEY_TIME = "time";
    private static final String KEY_DIFFICULTY = "difficulty";

    private Long time;

    public MinesweeperFragment() {
        // Required empty public constructor
    }

    public static MinesweeperFragment newInstance(OnePlayerGame minesweeper,
                                      Long time, Game.Difficulty difficulty) {
        Bundle args = new Bundle();

        args.putParcelable(KEY_MINESWEEPER, minesweeper);
        args.putLong(KEY_TIME, time);
        args.putSerializable(KEY_DIFFICULTY, difficulty);

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

        // get arguments
        Bundle args = getArguments();

        game = args.getParcelable(KEY_MINESWEEPER);
        time = args.getLong(KEY_TIME, 0L);
        difficulty = (Game.Difficulty) args.get(KEY_DIFFICULTY);

        gameCtrl = new GameController(mDifficulty, difficulty, getActivity());
        fieldCtrl = new FieldController(mRecyclerView, mMineField);
        timerCtrl = new TimerController(timerIcon, mTimer);
        minesCtrl = new MinesController(minesIcon, mMines, getActivity());
        flagCtrl = new FlagController(mFlag);

        if (savedInstanceState == null) {
            Log.i(TAG, "Starting a new game");
            // start a new game
            loadGame();
        } else {
            Log.i(TAG, "Loading a saved game");
            // load a previous instance of the game
            time = savedInstanceState.getLong(KEY_TIME);
            //timerCtrl.setUpdatedTime(savedInstanceState.getLong(KEY_TIME));
            game = savedInstanceState.getParcelable(KEY_MINESWEEPER);

            loadGame();
        }
    }

    public void loadGame() {

        timerCtrl.setUpdatedTime(time);

        // calculate how large a cell should be to fit 10 per row on the screen
        cellWidth = calculateCellWidth();

        gameCtrl.setGame(game);

        initFieldAdapter();
        setGameAdapters();

        fieldCtrl.setGameParams(cellWidth, game.getField().getDimX(), game.getField().getDimY());
        fieldCtrl.initViewSize(game.getField().getDimX(), game.getField().getDimY());

        minesCtrl.setGame(game);
        flagCtrl.setGame(game);

        mFieldAdapter.notifyDataSetChanged();
    }

    // calculate the size of a cell based on screen dpi
    // try to fit 10 on the screen
    private int calculateCellWidth() {
        DisplayMetrics display = getActivity().getResources().getDisplayMetrics();
        return Math.round(display.widthPixels / ((float) 10.5));
    }

    private void initFieldAdapter() {
        // create adapter to convert positions to points and points to positions
        mPositionAdapter = new PositionPointAdapter(game.getField().getDimX(), game.getField().getDimY());

        // create adapter to handle mine field
        mFieldAdapter = new FieldAdapter(getActivity(), game.getField(), cellWidth);
        mFieldAdapter.setPositionAdapter(mPositionAdapter);
        mFieldAdapter.setOnItemClickListener(this);
        mFieldAdapter.setOnItemLongClickListener(this);

        mRecyclerView.setAdapter(mFieldAdapter);
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

    public TimerController getTimerCtrl() { return timerCtrl; }

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

            gameCtrl.addRecord(null);

            MainActivity activity = (MainActivity) getActivity();
            activity.promptNewGame(R.string.lost_title, R.string.lost_message);

        } else if (result == Game.Status.WIN) {
            timerCtrl.stop();

            gameCtrl.addRecord(timerCtrl.getUpdatedTime());

            MainActivity activity = (MainActivity) getActivity();
            activity.promptNewGame(R.string.win_title, R.string.win_message);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "Saving instance state");

        // save game when screen is rotated

        outState.putLong(KEY_TIME, timerCtrl.getUpdatedTime());
        outState.putParcelable(KEY_MINESWEEPER, game);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Pausing..");

        if (game.isStarted() && !game.isFinished())
            timerCtrl.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "Resuming..");
        if (game.isStarted() && !game.isFinished())
            timerCtrl.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "Destroying..");

    }
}
