package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.models.Cell;

/**
 * Created by raymond on 2015-04-02.
 */
public interface Game {
    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public enum Status {
        NO_CHANGE, WIN, LOSE
    }

    public enum NotificationT {
        REVEAL, FLAG, UNFLAG, INVALID_REVEAL, INVALID_HIDDEN, MINE
    }

    public void startGame(int x, int y);
    public int reveal(int x, int y);
    public void flagCell(int x, int y, Cell.Status status);
    public Status checkGameFinished(Cell cell);

    public Status onClick(int x, int y);
    public boolean onLongClick(int x, int y);
}
