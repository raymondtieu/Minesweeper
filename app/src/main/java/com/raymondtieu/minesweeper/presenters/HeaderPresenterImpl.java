package com.raymondtieu.minesweeper.presenters;

import android.os.Bundle;

import com.raymondtieu.minesweeper.services.Observer;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.Notification;
import com.raymondtieu.minesweeper.views.HeaderView;

/**
 * Created by raymond on 2015-04-22.
 */
public class HeaderPresenterImpl implements HeaderPresenter, Observer {


    private HeaderView headerView;
    private OnePlayerGame minesweeper;

    public HeaderPresenterImpl(HeaderView view) {
        this.headerView = view;

        //init the game
        this.minesweeper = OnePlayerGame.getInstance(16, 16, 40, false);
        this.minesweeper.attach(this);
    }

    @Override
    public void initialize() {
        // init the game?


        // update the views
        headerView.updateMines(minesweeper.getNumMines());
        headerView.setFlag(minesweeper.isFlagging());
        // init the timer


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onToggleFlag() {

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
            // start timer
        }

        if (type == Notification.STOP_TIME) {
            // stop timer
        }
    }
}
