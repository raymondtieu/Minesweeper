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

        this.numFlags = 0;
        this.cellsRemaining = field.getNumCells() - mines;
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

    public void setMinesListener(MinesTextView minesListener) {
        this.minesListener = minesListener;
    }

    public void setPositionAdapter(PositionPointAdapter positionAdapter) {
        this.positionAdapter = positionAdapter;
    }

    public void setFieldAdapter(FieldAdapter fieldAdapter) {
        this.fieldAdapter = fieldAdapter;
    }

    public Field getField() {
        return this.field;
    }

    public void toggleFlag() {
        flagging = !flagging;
    }

    public Status onClick(int x, int y) {
        if (isFinished())
            return Status.NO_CHANGE;

        Cell cell = field.getCell(x, y);
        Cell.Status status = cell.getStatus();

        // handle a click event if the cell is hidden
        if (!isFlagging() && status == Cell.Status.HIDDEN) {
            if (!isStarted()) {
                startGame(x, y);
            }

            reveal(x, y);

            return checkGameFinished(cell);

        } else if (isFlagging() && status != Cell.Status.REVEALED) {
            // remove flag if there is already a flag
            if (status == Cell.Status.FLAGGED)
                flagCell(x, y, Cell.Status.HIDDEN);
            else if (status == Cell.Status.HIDDEN)
                flagCell(x, y, Cell.Status.FLAGGED);
        }


        return Status.NO_CHANGE;
    }


    public Status onLongClick(int x, int y) {
        if (isFinished())
            return Status.NO_CHANGE;

        Cell cell = field.getCell(x, y);

        if (cell.getStatus() == Cell.Status.FLAGGED) {
            // unflag cell
            flagCell(x, y, Cell.Status.HIDDEN);
        } else if (cell.getStatus() == Cell.Status.HIDDEN) {
            // flag cell
            flagCell(x, y, Cell.Status.FLAGGED);
        } else if (cell.getStatus() == Cell.Status.REVEALED) {
            // reveal all surrounding cells
            Status result = revealSurrounding(x, y);

            // if a mine wasn't uncovered, check if there are any cells
            // still hidden
            if (result != Status.LOSE) {
                result = checkGameFinished(cell);
            }

            return result;
        }

        return Status.NO_CHANGE;
    }

    @Override
    public void startGame(int x, int y) {
        field.generateField(x, y);

        setStarted(true);
        setFinished(false);
    }

    @Override
    public int reveal(int x, int y) {
        revealAdjacent(x, y, true);

        return 0;
    }

    public void revealAdjacent(int x, int y, boolean ignoreFlag) {
        Cell cell = field.getCell(x, y);

        // remove flag if cell is flagged
        if (cell.getStatus() == Cell.Status.FLAGGED && ignoreFlag) {
            flagCell(x, y, Cell.Status.HIDDEN);
        }

        // reveal this cell
        revealCell(x, y);

        // if this cell had no adjacent mines, reveal everything surrounding
        if (cell.getStatus() == Cell.Status.REVEALED && cell.getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {

                    // position for cell around this one
                    int v = x + i;
                    int w = y + j;

                    // reveal if adjacent position is not out of bounds
                    if (v >= 0 && v < field.getDimX() &&
                            w >= 0 && w < field.getDimY() &&
                            field.getCell(v, w).getStatus() != Cell.Status.REVEALED)
                        revealAdjacent(v, w, ignoreFlag);
                }
            }
        }
    }

    @Override
    public void flagCell(int x, int y, Cell.Status status) {
        Cell cell = field.getCell(x, y);

        if (status == Cell.Status.HIDDEN) {
            numFlags--;

            cell.setStatus(status);

            fieldAdapter.notifyFlagged(positionAdapter
                    .pointToPosition(new Point(x, y)), true);
        } else if (status == Cell.Status.FLAGGED && numFlags < field.getMines()) {
            numFlags++;

            cell.setStatus(status);

            fieldAdapter.notifyFlagged(positionAdapter
                    .pointToPosition(new Point(x, y)), false);
        }

        minesListener.onValueChanged(field.getMines() - numFlags);
    }


    public void revealCell(int x, int y) {
        Cell cell = field.getCell(x, y);

        if (cell.getStatus() == Cell.Status.HIDDEN) {
            cell.setStatus(Cell.Status.REVEALED);
            cellsRemaining--;

            fieldAdapter.notifyRevealed(positionAdapter
                    .pointToPosition(new Point(x, y)));
        }
    }


    @Override
    public Status checkGameFinished(Cell cell) {

        // revealed a mine
        if (cell.getAdjacentMines() >= 9) {
            setFinished(true);

            return Status.LOSE;

        // no more hidden cells
        } else if (cellsRemaining == 0) {
            setFinished(true);

            return Status.WIN;
        }

        return Status.NO_CHANGE;
    }

    public void revealAllMines() {
        for (int i = 0; i < field.getDimX(); i++) {
            for (int j = 0; j < field.getDimY(); j++) {
                Cell cell = field.getCell(i, j);

                if (cell.getAdjacentMines() >= 9) {
                    if (cell.getStatus() == Cell.Status.FLAGGED) {
                        cell.setStatus(Cell.Status.FLAG_CORRECT);

                        fieldAdapter.notifyRevealed(positionAdapter
                                .pointToPosition(new Point(i, j)));
                    } else {
                        cell.setStatus(Cell.Status.REVEALED);

                        fieldAdapter.notifyMine(positionAdapter
                                .pointToPosition(new Point(i, j)));
                    }
                } else if (cell.getStatus() == Cell.Status.FLAGGED) {
                    cell.setStatus(Cell.Status.FLAG_INCORRECT);

                    fieldAdapter.notifyRevealed(positionAdapter
                            .pointToPosition(new Point(i, j)));
                }
            }
        }
    }

    public Status revealSurrounding(int x, int y) {
        Cell cell = field.getCell(x, y);

        Status status = Status.NO_CHANGE;

        // count number of adjacent flags
        int flags = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int v = x + i;
                int w = y + j;

                if (v >= 0 && v < field.getDimX() &&
                        w >= 0 && w < field.getDimY() &&
                        field.getCell(v, w).getStatus() == Cell.Status.FLAGGED)
                    flags++;
            }
        }

        // can't reveal surrounding cells if number of flags doesn't match
        if (flags != cell.getAdjacentMines()) {
            return status;
            // animate current cell
        }

        // reveal all surrounding cells
        // only ones immediately surrounding can be mines
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int v = x + i;
                int w = y + j;

                if (v >= 0 && v < field.getDimX() &&
                        w >= 0 && w < field.getDimY() &&
                        field.getCell(v, w).getStatus() == Cell.Status.HIDDEN) {

                    if (field.getCell(v, w).getAdjacentMines() >= 9) {
                        status = Status.LOSE;
                        setFinished(true);
                    }

                    revealAdjacent(v, w, false);
                }
            }
        }

        return status;
    }
}
