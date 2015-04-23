package com.raymondtieu.minesweeper.services;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.raymondtieu.minesweeper.models.Cell;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.utils.GameUtils;

/**
 * Created by raymond on 2015-04-12.
 */
public class OnePlayerGame extends Observable implements Game, Parcelable {

    private static final String TAG = "1PGAME";

    private boolean started;
    private boolean finished;
    private boolean flagging;

    private Field field;

    private int numFlags;
    private int cellsRemaining;

    private GameUtils gameUtils;

    private static OnePlayerGame minesweeper;


    protected OnePlayerGame(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
        initializeNewGame();
    }

    /* Create a game given a field from a save state */
    protected OnePlayerGame(Field field, String difficulty) {
        this.field = field;
        started = true;
        finished = false;
        flagging = false;

        numFlags = 0;
        cellsRemaining = field.getNumCells() - field.getMines();

        for (int i = 0; i < field.getDimX(); i++) {
            for (int j = 0; j < field.getDimY(); j++) {
                Cell.Status status  = field.getCell(i, j).getStatus();
                if (status == Cell.Status.REVEALED)
                    cellsRemaining--;
                else if (status == Cell.Status.FLAGGED)
                    numFlags++;
            }
        }

        gameUtils = new GameUtils(difficulty);

        notifynMines(getNumMines());
    }

    public static OnePlayerGame getInstance() {
        return minesweeper;
    }

    public static OnePlayerGame getInstance(GameUtils gameUtils, boolean newGame) {
        if (minesweeper == null || newGame) {
            Log.i(TAG, "Creating new instance");
            minesweeper = new OnePlayerGame(gameUtils);
        } else
            Log.i(TAG, "instance exists");

        return minesweeper;
    }

    public static OnePlayerGame getInstance(Field field, String difficulty) {
        if (minesweeper == null) {
            Log.i(TAG, "Creating new instance with field");
            minesweeper = new OnePlayerGame(field, difficulty);
        } else
            Log.i(TAG, "Instance with field already exists");

        return minesweeper;
    }


    private void initializeNewGame() {
        setStarted(false);
        setFinished(false);
        flagging = false;
        numFlags = 0;
        cellsRemaining = gameUtils.getNumCells() - gameUtils.getnMines();

        field = new Field(gameUtils.getDimX(), gameUtils.getDimY(), gameUtils.getnMines());

        notifynMines(getNumMines());
    }


    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;

        if (this.started)
            notifyStartTime();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;

        if (this.finished)
            notifyStopTime();
    }

    public boolean isFlagging() {
        return flagging;
    }

    public int getNumMines() {
        return gameUtils.getnMines() - numFlags;
    }

    public Field getField() {
        return this.field;
    }

    public GameUtils getGameUtils() {
        return this.gameUtils;
    }

    public void toggleFlag() {
        flagging = !flagging;

        int value = (isFlagging())? 1 : 0;

        notifyToggleFlag(value);
    }

    @Override
    public void onClick(int x, int y) {
        Cell cell = field.getCell(x, y);
        Cell.Status status = cell.getStatus();

        if (status == Cell.Status.HIDDEN) {
            if (isFlagging()) {
                // add flag
                flagCell(x, y, Cell.Status.FLAGGED);
            } else {
                // start game if needed
                if (!isStarted())
                    startGame(x, y);

                reveal(x, y);

                checkGameFinished(cell);
            }

        } else if (status == Cell.Status.REVEALED) {
            // toggle flag mode on empty cell click
            if (cell.getAdjacentMines() <= 0) {
                toggleFlag();

                // reveal surrounding cells when cell is pressed
            } else {
                revealSurrounding(x, y);

                // if a mine was not found, check to see if game is won
                if (!isFinished())
                    checkGameFinished(cell);
            }

        } else if (status == Cell.Status.FLAGGED) {
            // remove flag if flagging is on
            if (isFlagging())
                flagCell(x, y, Cell.Status.HIDDEN);
        }
    }

    @Override
    public boolean onLongClick(int x, int y) {
        Cell cell = field.getCell(x, y);

        if (cell.getStatus() == Cell.Status.FLAGGED) {
            // unflag cell
            flagCell(x, y, Cell.Status.HIDDEN);

            return true;

        } else if (cell.getStatus() == Cell.Status.HIDDEN) {
            // flag cell
            flagCell(x, y, Cell.Status.FLAGGED);

            return true;
        }

        return false;
    }

    @Override
    public void startGame(int x, int y) {
        field.generateField(x, y);

        setStarted(true);
        setFinished(false);
    }

    @Override
    public void reveal(int x, int y) {
        revealAdjacent(x, y, true);
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

        // unflagging cell
        if (status == Cell.Status.HIDDEN) {
            numFlags--;
            cell.setStatus(status);

            notifyUnflag(gameUtils.getPosition(x, y));

        // flagging cell
        } else if (status == Cell.Status.FLAGGED) {

            if (numFlags < field.getMines()) {
                numFlags++;
                cell.setStatus(status);

                notifyFlagged(gameUtils.getPosition(x, y));
            } else {
                notifyInvalidHidden(gameUtils.getPosition(x, y));
            }
        }

        notifynMines(getNumMines());
    }


    public void revealCell(int x, int y) {
        Cell cell = field.getCell(x, y);

        if (cell.getStatus() == Cell.Status.HIDDEN) {
            cell.setStatus(Cell.Status.REVEALED);
            cellsRemaining--;

            if (cell.getAdjacentMines() < 9) {
                notifyRevealed(gameUtils.getPosition(x, y));
            } else {
                notifyMine(gameUtils.getPosition(x, y));
            }
        }
    }


    @Override
    public void checkGameFinished(Cell cell) {

        // revealed a mine
        if (cell.getAdjacentMines() >= 9) {
            setFinished(true);
            notifyLose();
        // no more hidden cells
        } else if (cellsRemaining == 0) {
            setFinished(true);
            notifyWin();
        }
    }

    public void revealAllMines() {
        for (int i = 0; i < field.getDimX(); i++) {
            for (int j = 0; j < field.getDimY(); j++) {
                Cell cell = field.getCell(i, j);

                if (cell.getAdjacentMines() >= 9) {
                    if (cell.getStatus() == Cell.Status.FLAGGED) {
                        cell.setStatus(Cell.Status.FLAG_CORRECT);

                        notifyFlagged(gameUtils.getPosition(i, j));
                    } else {
                        cell.setStatus(Cell.Status.REVEALED);

                        notifyMine(gameUtils.getPosition(i, j));
                    }
                } else if (cell.getStatus() == Cell.Status.FLAGGED) {
                    cell.setStatus(Cell.Status.FLAG_INCORRECT);

                    notifyFlagged(gameUtils.getPosition(i, j));
                }
            }
        }
    }

    public void revealSurrounding(int x, int y) {
        Cell cell = field.getCell(x, y);

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
            notifyInvalidRevealed(gameUtils.getPosition(x, y));

        } else {
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
                            setFinished(true);
                            notifyLose();
                        }

                        revealAdjacent(v, w, false);
                    }
                }
            }
        }
    }


    /* PARCELABLE METHODS */

    protected OnePlayerGame(Parcel in) {
        started = in.readByte() != 0;
        finished = in.readByte() != 0;
        flagging = in.readByte() != 0;

        numFlags = in.readInt();
        cellsRemaining = in.readInt();

        field = in.readParcelable(Field.class.getClassLoader());
        gameUtils = in.readParcelable(GameUtils.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (started ? 1 : 0));
        dest.writeByte((byte) (finished ? 1 : 0));
        dest.writeByte((byte) (flagging ? 1 : 0));

        dest.writeInt(numFlags);
        dest.writeInt(cellsRemaining);

        dest.writeParcelable(field, flags);
        dest.writeParcelable(gameUtils, flags);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OnePlayerGame> CREATOR = new Parcelable.Creator<OnePlayerGame>() {
        @Override
        public OnePlayerGame createFromParcel(Parcel in) {
            return new OnePlayerGame(in);
        }

        @Override
        public OnePlayerGame[] newArray(int size) {
            return new OnePlayerGame[size];
        }
    };
}
