package com.raymondtieu.minesweeper.services;

/**
 * Created by raymond on 2015-04-02.
 */

import android.graphics.Point;

import com.raymondtieu.minesweeper.adapters.CellAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.models.*;

public class OnePlayerGame implements Game {	
    // timer
    private boolean started;
    private boolean finished;
    private Board board;

    private CellAdapter adapter;
    private PositionPointAdapter positionAdapter;

    public OnePlayerGame(int dx, int dy, int m) {
        this.board = new Board(dx, dy, m);
        this.started = false;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isStarted() { return started; }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public void startGame(int x, int y) {
        this.board.generateBoard(x, y);
        this.finished = false;
        this.started = true;

        // reveal blocks surrounding starting position and everything around 0 blocks in
        // reach
        revealAdjacent(x, y);
    }


    // adapter to notify recycle viewer of any revealed cells
    @Override
    public boolean revealCell(int x, int y) {
        boolean m = this.board.revealCell(x, y) >= 9;

        adapter.notifyAndAnimate(positionAdapter.pointToPosition(new Point(x, y)));

        // selected a mine
        if (m) {
            setFinished(true);
        } else {
            revealAdjacent(x, y);
        }

        return m;
    }

    /* x and y are center, reveal everything around them if no surrounding mines recursively */
    @Override
    public void revealAdjacent(int x, int y) {

        // reveal all blocks around any block with no adjacent mines
        if (this.board.revealCell(x, y) == 0) {

            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    int s_x = x + a;
                    int s_y = y + b;

                    // check out of bounds
                    if (s_x >= 0 && s_x < board.getxDimension() &&
                            s_y >= 0 && s_y < board.getyDimension()) {

                        // also reveal any blocks adjacent to this one with no mines
                        if (board.getNumMines(s_x, s_y) == 0 && !board.isRevealed(s_x, s_y)) {
                            revealAdjacent(s_x, s_y);
                        }

                        board.revealCell(s_x, s_y);
                        adapter.notifyAndAnimate(positionAdapter.pointToPosition(new Point(s_x, s_y)));
                    }
                }
            }
        }
    }

    @Override
    public void markCell(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void markAdjacent(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean gameOver() {
        return false;
    }

    public void revealAllMines() {
        for (int i = 0; i < board.getxDimension(); i++) {
            for (int j = 0; j < board.getyDimension(); j++) {
                if (board.getNumMines(i, j) >= 9)
                    board.revealCell(i, j);
            }
        }
    }


    public void setCellAdapter(CellAdapter adapter) {
        this.adapter = adapter;
    }

    public void setPositionAdapter(PositionPointAdapter adapter) {
        this.positionAdapter = adapter;
    }
}