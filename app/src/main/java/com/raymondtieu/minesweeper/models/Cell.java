package com.raymondtieu.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raymond on 2015-04-12.
 */
public class Cell implements Parcelable {

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


    /* PARCELABLE METHODS */

    protected Cell(Parcel in) {
        adjacentMines = in.readInt();
        status = (Status) in.readValue(Status.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adjacentMines);
        dest.writeValue(status);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>() {
        @Override
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
