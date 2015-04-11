package com.raymondtieu.minesweeper.services;

/**
 * Created by raymond on 2015-04-02.
 */

import android.graphics.Point;

import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.models.*;

public class OnePlayerGame implements Game {	
    // timer
    private boolean started;
    private boolean finished;
    private boolean flagMode;

    private Field field;

    private FieldAdapter fieldAdapter;
    private PositionPointAdapter positionAdapter;
    private Field.MinesChangedListener minesListener;

    public OnePlayerGame(int dx, int dy, int m) {
        field = new Field(dx, dy, m);
        field.setGame(this);
        started = false;
        finished = false;
        flagMode = false;
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

        reveal(x, y);
    }


    public int onClick(int x, int y) {
        if (isFinished())
            return 0;

        if (flagMode) {
            markCell(x, y);
        } else if (!field.isFlagged(x, y)) {

            if (!isStarted()) {
                startGame(x, y);
                return 0;
            }

            return reveal(x, y);
        }

        return 0;
    }

    public boolean onLongClick(int x, int y) {
        if (isFinished())
            return false;

        markCell(x, y);
        return true;
    }

    @Override
    public int reveal(int x, int y) {
        int n = field.reveal(x, y);

        // selected a mine
        if (n >= 9) {
            setFinished(true);
        } else if (field.getCellsHidden() == 0) {
            setFinished(true);
            return -1;
        }

        return n;
    }

    @Override
    public void markCell(int x, int y) {
        if (field.isFlagged(x, y)) {
            // if there's a flag at x, y, remove it
            field.setFlag(x, y, -1);
        } else {
            field.setFlag(x, y, 1);
        }
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
    public void notifyFlagged(int x, int y, boolean isFlagged) {
        fieldAdapter.notifyFlagged(positionAdapter
                .pointToPosition(new Point(x, y)), isFlagged);
    }

    public void revealAllMines() {
        for (int i = 0; i < field.getDimX(); i++) {
            for (int j = 0; j < field.getDimY(); j++) {
                if (field.getNumMines(i, j) >= 9) {
                    if (field.isFlagged(i, j))
                        field.setFlag(i, j, 2);
                    else
                        field.reveal(i, j);

                    fieldAdapter.notifyItemChanged(positionAdapter
                        .pointToPosition(new Point(i, j)));
                } else if (field.isFlagged(i, j)) {
                    field.setFlag(i, j, -1);

                    fieldAdapter.notifyItemChanged(positionAdapter
                            .pointToPosition(new Point(i, j)));
                }
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

    public void setMinesListener(MinesTextView m) {
        minesListener = m;
    }

    @Override
    public void notifyMinesLeft(int n) {
        minesListener.onValueChanged(n);
    }
}