package com.raymondtieu.minesweeper.presenters;

import android.os.Bundle;

/**
 * Created by raymond on 2015-04-22.
 */
public interface HeaderPresenter {

    public void onActivityCreated(Bundle savedInstanceState, Bundle args);

    public void onResume();

    public void onPause();

    public void onSaveInstanceState(Bundle outState, Long time);

    public void onToggleFlag();

    public void startNewGame();
}
