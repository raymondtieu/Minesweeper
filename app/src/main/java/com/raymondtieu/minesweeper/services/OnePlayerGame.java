package com.raymondtieu.minesweeper.services;

/**
 * Created by raymond on 2015-04-02.
 */

import android.graphics.Point;

import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.models.*;

public class OnePlayerGame implements Game {	
    // timer
    private boolean started;
    private boolean finished;
    private boolean flagMode;

    private Field field;

    private FieldAdapter fieldAdapter;
    private PositionPointAdapter positionAdapter;

    private int minesLeft;

    public OnePlayerGame(int dx, int dy, int m) {
        field = new Field(dx, dy, m);
        field.setGame(this);
        started = false;
        finished = false;
        flagMode = false;

        minesLeft = m;
    }

    public Field getField() {
        return this.field;
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
        this.field.generateField(x, y);
        this.finished = false;
        this.started = true;
        this.flagMode = false;

        // reveal blocks surrounding starting position
        field.reveal(x, y);
    }


    @Override
    public int reveal(int x, int y) {
        if (!flagMode && !field.isFlagged(x, y)) {
            int n = field.reveal(x, y);

            // selected a mine
            if (n >= 9) {
                setFinished(true);
            } else if (field.getCellsHidden() == 0) {
                setFinished(true);
                return -1;
            }

            return n;
        } else if (flagMode) {
            if (field.isFlagged(x, y))
                field.setFlag(x, y, false);
            else
                field.setFlag(x, y, true);

        }

        return 0;
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

    @Override
    public void notifyRevealed(int x, int y) {
        fieldAdapter.notifyRevealed(positionAdapter
                .pointToPosition(new Point(x, y)));
    }

    @Override
    public void notifyFlagged(int x, int y) {
        fieldAdapter.notifyFlagged(positionAdapter
                .pointToPosition(new Point(x, y)));
    }

    public void revealAllMines() {
        for (int i = 0; i < field.getDimX(); i++) {
            for (int j = 0; j < field.getDimY(); j++) {
                if (field.getNumMines(i, j) >= 9)
                    field.setRevealed(i, j);
            }
        }
    }


    public void setFieldAdapter(FieldAdapter adapter) {
        fieldAdapter = adapter;
    }

    public void setPositionAdapter(PositionPointAdapter adapter) {
        this.positionAdapter = adapter;
    }

    public void toggleFlag() {
        flagMode = !flagMode;
    }

    public boolean isFlagMode() {
        return flagMode;
    }

    public int getMinesLeft() {
        return minesLeft;
    }
}