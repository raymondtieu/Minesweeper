package com.raymondtieu.minesweeper.presenters;

import android.graphics.Point;
import android.os.Bundle;

import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.Observer;
import com.raymondtieu.minesweeper.services.OnePlayerGame;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.utils.Notification;
import com.raymondtieu.minesweeper.views.MineFieldView;


/**
 * Created by raymond on 2015-04-22.
 */
public class MineFieldPresenterImpl implements MineFieldPresenter, Observer {

    private MineFieldView mineFieldView;
    private GameUtils gameUtils;
    private OnePlayerGame minesweeper;

    public MineFieldPresenterImpl(MineFieldView view) {
        this.mineFieldView = view;

        this.minesweeper = OnePlayerGame.getInstance(16, 16, 40, false);
        this.minesweeper.attach(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void initialize() {
        gameUtils = minesweeper.getGameUtils();
        mineFieldView.setUpMineField(minesweeper.getField(), gameUtils);

        mineFieldView.setMineFieldViewSize(minesweeper.getField().getDimX(),
                minesweeper.getField().getDimY());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public Game.Status onClick(int position) {
        Point p = gameUtils.getPoint(position);

        return minesweeper.onClick(p.x, p.y);
    }

    @Override
    public boolean onLongClick(int position) {
        Point p = gameUtils.getPoint(position);

        return minesweeper.onLongClick(p.x, p.y);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void update(Notification type, int value) {
        if (type != Notification.NUM_MINES && type != Notification.TOGGLE_FLAG
                && type != Notification.START_TIME && type != Notification.STOP_TIME)
            mineFieldView.updateField(type, value);
    }
}
