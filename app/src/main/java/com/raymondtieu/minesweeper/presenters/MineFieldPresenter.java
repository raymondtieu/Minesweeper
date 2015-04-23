package com.raymondtieu.minesweeper.presenters;

import android.os.Bundle;

import com.raymondtieu.minesweeper.services.Game;

/**
 * Created by raymond on 2015-04-22.
 */
public interface MineFieldPresenter {

    public void onActivityCreated(Bundle savedInstanceState);

    public void onSaveInstanceState(Bundle outState);

    public void onResume();

    public Game.Status onClick(int position);

    public boolean onLongClick(int position);

    public void onFinish();
}
