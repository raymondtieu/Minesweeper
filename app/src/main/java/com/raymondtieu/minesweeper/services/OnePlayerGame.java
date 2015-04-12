package com.raymondtieu.minesweeper.services;

import android.graphics.Point;

import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.models.Cell;
import com.raymondtieu.minesweeper.models.Field;

/**
 * Created by raymond on 2015-04-12.
 */
public class OnePlayerGame implements Game {
    private boolean started;
    private boolean finished;
    private boolean flagging;

    private Field field;

    private int minesRemaining;
    private int numFlags;
    private int cellsRemaining;

    private FieldAdapter fieldAdapter;
    private PositionPointAdapter positionAdapter;
    private MinesTextView minesListener;

    public OnePlayerGame(int dimX, int dimY, int mines) {
        this.started = false;
        this.finished = false;
        this.flagging = false;

        this.field = new Field(dimX, dimY, mines);

        this.minesRemaining = mines;
        this.numFlags = 0;
        this.cellsRemaining = field.getNumCells();
    }


    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFlagging() {
        return flagging;
    }

    public void setFlagging(boolean flagging) {
        this.flagging = flagging;
    }

    public void setMinesListener(MinesTextView minesListener) {
        this.minesListener = minesListener;
    }

    public void setPositionAdapter(PositionPointAdapter positionAdapter) {
        this.positionAdapter = positionAdapter;
    }

    public void setFieldAdapter(FieldAdapter fieldAdapter) {
        this.fieldAdapter = fieldAdapter;
    }

    public GameStatus onClick(int x, int y) {
        if (isFinished())
            return GameStatus.NO_CHANGE;

        Cell cell = field.getCell(x, y);

        // handle a click event if the cell is hidden
        if (!isFlagging() && cell.getStatus() == Cell.Status.HIDDEN) {
            if (!isStarted()) {
                startGame(x, y);
            }

            reveal(x, y);

            return checkGameFinished(cell);

        } else if (isFlagging()) {
            // remove flag if there is already a flag
            if (cell.getStatus() == Cell.Status.FLAGGED)
                flagCell(x, y, Cell.Status.HIDDEN);
            else
                flagCell(x, y, Cell.Status.FLAGGED);
        }


        return GameStatus.NO_CHANGE;
    }


    public void onLongClick(int x, int y) {

    }

    @Override
    public void startGame(int x, int y) {
        field.generateField(x, y);

        setStarted(true);
        setFinished(false);
    }

    @Override
    public int reveal(int x, int y) {
        revealAdjacent(x, y);

        return 0;
    }

    public void revealAdjacent(int x, int y) {
        Cell cell = field.getCell(x, y);

        // remove flag if cell is flagged
        if (cell.getStatus() == Cell.Status.FLAGGED) {
            numFlags--;
        }

        // reveal this cell
        revealCell(cell, x, y);


        // if this cell had no adjacent mines, reveal everything surrounding
        if (cell.getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    // position for cell around this one
                    int v = x + i;
                    int w = y + j;

                    // reveal if adjacent position is not out of bounds
                    if (v >= 0 && v < field.getDimX() &&
                            w >= 0 && w < field.getDimY() &&
                            field.getCell(v, w).getStatus() == Cell.Status.HIDDEN) {

                        revealAdjacent(v, w);
                    }
                }
            }
        }
    }

    @Override
    public void flagCell(int x, int y, Cell.Status status) {
        Cell cell = field.getCell(x, y);

        cell.setStatus(status);

        if (status == Cell.Status.FLAGGED) {
            numFlags--;
            fieldAdapter.notifyFlagged(positionAdapter
                    .pointToPosition(new Point(x, y)), false);
        } else if (status == Cell.Status.HIDDEN) {
            numFlags++;
            fieldAdapter.notifyFlagged(positionAdapter
                    .pointToPosition(new Point(x, y)), true);
        }

        minesListener.onValueChanged(minesRemaining - numFlags);
    }


    public void revealCell(Cell cell, int x, int y) {
        cell.setStatus(Cell.Status.REVEALED);
        cellsRemaining--;

        fieldAdapter.notifyItemChanged(positionAdapter
                .pointToPosition(new Point(x, y)));
    }


    @Override
    public GameStatus checkGameFinished(Cell cell) {

        // revealed a mine
        if (cell.getAdjacentMines() >= 9) {
            setFinished(true);

            return GameStatus.LOSE;

        // no more hidden cells
        } else if (cellsRemaining == 0) {
            setFinished(true);

            return GameStatus.WIN;
        }

        return GameStatus.NO_CHANGE;
    }
}
