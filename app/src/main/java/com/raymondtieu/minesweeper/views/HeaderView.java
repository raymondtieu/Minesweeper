package com.raymondtieu.minesweeper.views;

/**
 * Created by raymond on 2015-04-22.
 */
public interface HeaderView {
    public void setDifficulty(String difficulty);

    public void updateMines(int numMines);

    public void setFlag(boolean flag);

    public void setTimer(Long time, boolean blink);

    public void startTimer();

    public void pauseTimer();

    public Long stopTimer();

    public void initDatabase();

    public void insertDatabase(String difficulty, long date, Long time);
}
