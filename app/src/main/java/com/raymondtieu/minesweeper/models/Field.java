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
    private int mines;

    private Cell[][] field;

    public Field(int x, int y, int mines) {
        this.dimX = x;
        this.dimY = y;
        this.mines = mines;

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
        return mines;
    }

    public int getNumCells() {
        return dimX * dimY;
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public void generateField(int x, int y) {
        int n = this.mines;

        // place mines on blank field until there are no mines left to be placed
        // generate i and j value until position without mine is found
        while (n > 0) {
            int a = (int) (Math.random() * dimX);
            int b = (int) (Math.random() * dimY);

            // true when i and j are surrounding x and y
            boolean adjacentX = Math.abs(x - a) <= 1;
            boolean adjacentY = Math.abs(y - b) <= 1;

            // check for valid coordinates (not starting point or around it)
            if (!(adjacentX && adjacentY)) {

                // if no mine at (a,b), set mine
                if (field[a][b].getAdjacentMines() < 9) {
                    field[a][b].setAdjacentMines(9);

                    // update surrounding blocks
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int v = a + i;
                            int w = b + j;

                            // check out of bounds
                            if (v >= 0 && v < dimX && w >= 0 && w < dimY)
                                field[v][w].addAdjacentMine();
                        }
                    }

                    n--;
                }
            }
        }
    }


    /* PARCELABLE METHODS */

    protected Field(Parcel in) {
        dimX = in.readInt();
        dimY = in.readInt();
        mines = in.readInt();

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
        dest.writeInt(mines);

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


