package com.raymondtieu.minesweeper.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.raymondtieu.minesweeper.controllers.FieldController;
import com.raymondtieu.minesweeper.controllers.FlagController;
import com.raymondtieu.minesweeper.controllers.GameController;
import com.raymondtieu.minesweeper.controllers.MinesController;
import com.raymondtieu.minesweeper.controllers.TimerController;
import com.raymondtieu.minesweeper.layouts.FixedGridLayoutManager;
import com.raymondtieu.minesweeper.layouts.FlagImageView;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.layouts.TimerImageView;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.presenters.MineFieldPresenter;
import com.raymondtieu.minesweeper.presenters.MineFieldPresenterImpl;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.utils.Notification;
import com.raymondtieu.minesweeper.views.MineFieldView;

/**
 * Created by raymond on 2015-04-22.
 */
public class MineFieldFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, MineFieldView {

    private MineFieldPresenter presenter;

    private RecyclerView mRecyclerView;
    private FieldAdapter mAdapter;
    private FrameLayout mFrameLayout;

    private int cellDimen = 90;

    public static MineFieldFragment newInstance(OnePlayerGame minesweeper,
                                                  Long time, Game.Difficulty difficulty) {
        Bundle args = new Bundle();

        args.putParcelable(KEY_MINESWEEPER, minesweeper);
        args.putLong(KEY_TIME, time);
        args.putSerializable(KEY_DIFFICULTY, difficulty);

        MineFieldFragment f = new MineFieldFragment();

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
                .inflate(R.layout.fragment_minefield, container, false);

        // set views
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.minefield);
        mFrameLayout = (FrameLayout) layout.findViewById(R.id.minefield_container);

        if (presenter == null)
            presenter = new MineFieldPresenterImpl(this);

        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(savedInstanceState);

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

    @Override
    public void setUpMineField(Field field, GameUtils gameUtils) {
        mAdapter = new FieldAdapter(getActivity(), field, cellDimen);

        mAdapter.setPositionAdapter(gameUtils);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(field.getDimY());

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void updateField(Notification type, int position) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return presenter.onLongClick(position);
    }
}
