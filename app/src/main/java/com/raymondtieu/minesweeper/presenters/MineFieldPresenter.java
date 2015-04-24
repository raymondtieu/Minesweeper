package com.raymondtieu.minesweeper.presenters;

import android.os.Bundle;

/**
 * Created by raymond on 2015-04-22.
 */
public interface MineFieldPresenter {

    public void onActivityCreated(Bundle savedInstanceState);

    public void onSaveInstanceState(Bundle outState);

    public void onResume();

    public void onClick(int position);

    public boolean onLongClick(int position);

    public void startNewGame();
}
