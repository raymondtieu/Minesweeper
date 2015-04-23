package com.raymondtieu.minesweeper.services;

import com.raymondtieu.minesweeper.models.Cell;

/**
 * Created by raymond on 2015-04-02.
 */
public interface Game {


    public void startGame(int x, int y);
    public void reveal(int x, int y);
    public void flagCell(int x, int y, Cell.Status status);
    public void checkGameFinished(Cell cell);

    public void onClick(int x, int y);
    public boolean onLongClick(int x, int y);
}
