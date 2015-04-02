package com.raymondtieu.minesweeper.services;

/**
 * Created by raymond on 2015-04-02.
 */
public interface Game {
    public void startGame(int x, int y);
    public boolean revealCell(int x, int y);
    public void revealAdjacent(int x, int y);
    public void markCell(int x, int y);
    public void markAdjacent(int x, int y);
    public boolean gameOver();
}
