package com.raymondtieu.minesweeper.models;

/**
 * Created by raymond on 2015-04-12.
 */
public class Cell {

    public enum Status {
        HIDDEN, REVEALED, FLAGGED, FLAG_CORRECT, FLAG_INCORRECT
    }

    private int adjacentMines;
    private Status status;

    public Cell() {
        this.adjacentMines = 0;
        this.status = Status.HIDDEN;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addAdjacentMine() {
        adjacentMines++;
    }
}
