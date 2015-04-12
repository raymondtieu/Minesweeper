package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.models.Cell;

/**
 * Created by raymond on 2015-04-02.
 */
public interface Game {
    public enum GameStatus {
        NO_CHANGE, WIN, LOSE
    }

    public void startGame(int x, int y);
    public int reveal(int x, int y);
    public void flagCell(int x, int y, Cell.Status status);
    public GameStatus checkGameFinished(Cell cell);
}
