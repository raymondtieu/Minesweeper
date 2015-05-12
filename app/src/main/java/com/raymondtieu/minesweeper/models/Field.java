package com.raymondtieu.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by raymond on 2015-04-02.
 */
public class Field implements Parcelable {

    private int dimX;
    private int dimY;
    private int nMines;

    private Cell[][] field;

    public Field(int x, int y, int nMines) {
        this.dimX = x;
        this.dimY = y;
        this.nMines = nMines;

        this.field = new Cell[dimX][dimY];

        // generate blank field with empty cells
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                field[i][j] = new Cell();
            }
        }
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getMines() {
        return nMines;
    }

    public int getNumCells() {
        return dimX * dimY;
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public void generateField(int startX, int startY) {
        int minesLeft = this.nMines;

        // place mines in random locations on field until there are none left
        while (minesLeft > 0) {
            int x = (int) (Math.random() * dimX);
            int y = (int) (Math.random() * dimY);

            // true when (x, y) is adjacent to starting point
            boolean adjacentX = Math.abs(startX - x) <= 1;
            boolean adjacentY = Math.abs(startY - y) <= 1;

            // place a mine only if it is not adjacent to the starting point
            // and if (x, y) is not already a mine
            if (!(adjacentX && adjacentY) && field[x][y].getAdjacentMines() < 9) {
                field[x][y].setAdjacentMines(9);

                // increment number of adjacent mines around cell
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int v = x + i;
                        int w = y + j;

                        // check out of bounds
                        if (v >= 0 && v < dimX && w >= 0 && w < dimY)
                            field[v][w].addAdjacentMine();
                    }
                }

                minesLeft--;
            }
        }
    }


    /* PARCELABLE METHODS */

    protected Field(Parcel in) {
        dimX = in.readInt();
        dimY = in.readInt();
        nMines = in.readInt();

        field = new Cell[dimX][dimY];

        // read the field from parcel one row of cells at a time
        for (int i = 0; i < dimX; i++) {
            in.readTypedArray(field[i], Cell.CREATOR);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dimX);
        dest.writeInt(dimY);
        dest.writeInt(nMines);

        // write the field to dest one row of cells at a time
        for (int i = 0; i < dimX; i++) {
            dest.writeTypedArray(field[i], flags);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        @Override
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}


