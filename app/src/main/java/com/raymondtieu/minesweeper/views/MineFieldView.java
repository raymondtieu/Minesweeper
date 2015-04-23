package com.raymondtieu.minesweeper.views;

import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.utils.Notification;

/**
 * Created by raymond on 2015-04-22.
 */
public interface MineFieldView {

    public void setMineFieldViewSize(int x, int y);

    public void setUpMineField(Field field, GameUtils gameUtils);

    public void updateField(Notification type, int position);

    public void onWin();

    public void onLose();
}
