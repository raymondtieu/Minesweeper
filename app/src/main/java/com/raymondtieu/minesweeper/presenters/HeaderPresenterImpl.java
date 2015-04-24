package com.raymondtieu.minesweeper.presenters;

import android.os.Bundle;
import android.util.Log;

import com.raymondtieu.minesweeper.services.Observer;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.Notification;
import com.raymondtieu.minesweeper.views.HeaderView;

/**
 * Created by raymond on 2015-04-22.
 */
public class HeaderPresenterImpl implements HeaderPresenter, Observer {

    private static final String TAG = "HEADERPRESENTER";

    private static final String KEY_TIME = "time";

    private HeaderView headerView;
    private OnePlayerGame minesweeper;

    private Long currentTime = 0L;

    public HeaderPresenterImpl(HeaderView view) {
        this.headerView = view;
    }


    private void initialize() {
        // init the game
        minesweeper = OnePlayerGame.getInstance();
        minesweeper.attach(this);

        // update the views
        headerView.setDifficulty(minesweeper.getGameUtils().getDifficulty());
        headerView.updateMines(minesweeper.getNumMines());
        headerView.setFlag(minesweeper.isFlagging());

        // init the timer
        if (minesweeper.isFinished())
            headerView.setTimer(currentTime, true);
        else
            headerView.setTimer(currentTime, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState, Bundle args) {

        // get arguments from bundle
        currentTime = args.getLong(KEY_TIME, 0L);

        // get the time from bundle
        if (savedInstanceState != null) {
            currentTime = savedInstanceState.getLong(KEY_TIME);
        }

        initialize();
    }

    @Override
    public void onResume() {
        if (minesweeper.isStarted() && !minesweeper.isFinished())
            headerView.startTimer();
    }

    @Override
    public void onPause() {
        if (minesweeper.isStarted() && !minesweeper.isFinished())
            headerView.pauseTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, Long time) {

        // save the time in here and put it back onresume
        outState.putLong(KEY_TIME, time);
    }

    @Override
    public void onToggleFlag() {
        if (!minesweeper.isFinished())
            minesweeper.toggleFlag();
    }

    private void onFinish() {

        // stop listening to game
        minesweeper.detach(this);
    }

    @Override
    public void startNewGame() {
        currentTime = 0L;
        initialize();
    }

    @Override
    public void update(Notification type, int value) {
        if (type == Notification.NUM_MINES)
            headerView.updateMines(value);


        if (type == Notification.TOGGLE_FLAG) {
            boolean flag = value != 0;

            headerView.setFlag(flag);
        }

        if (type == Notification.START_TIME) {
            Log.i(TAG, "Start timer");
            headerView.startTimer();
        }

        if (type == Notification.STOP_TIME) {
            currentTime = headerView.stopTimer();

            Log.i(TAG, "Stop timer " + currentTime);
        }

        if (type == Notification.WIN) {
            Log.i(TAG, "Game is won");
            onFinish();
        }

        if (type == Notification.LOSE) {
            Log.i(TAG, "Game is lost");
            onFinish();
        }
    }
}
