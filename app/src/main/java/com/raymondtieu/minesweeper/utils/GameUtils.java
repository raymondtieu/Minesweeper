package com.raymondtieu.minesweeper.utils;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by raymond on 2015-04-22.
 */
public class GameUtils implements Parcelable {


    public static final String PREF_FILE = "pref_minesweeper";

    public static final String BEGINNER = "beginner";
    public static final String INTERMEDIATE = "intermediate";
    public static final String ADVANCED = "advanced";

    private int dimX;
    private int dimY;
    private int nMines;
    private String difficulty;

    public GameUtils(String difficulty) {
        switch(difficulty.toLowerCase()) {
            case BEGINNER:
                this.difficulty = BEGINNER;
                dimX = 9;
                dimY = 9;
                nMines = 10;
                break;
            case ADVANCED:
                this.difficulty = ADVANCED;
                dimX = 16;
                dimY = 30;
                nMines = 99;
            default:
                this.difficulty = INTERMEDIATE;
                dimX = 16;
                dimY = 16;
                nMines = 40;
        }
    }

    public Point getPoint(int position) {
        int i = position / dimY;
        int j = position % dimY;

        return new Point(i, j);
    }

    public int getPosition(int x, int y) {
        return x * dimY + y;
    }

    public int getDimX() {
        return dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public int getnMines() {
        return nMines;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getNumCells() {
        return dimX * dimY;
    }


    /* PARCELABLE METHODS */

    protected GameUtils(Parcel in) {
        dimX = in.readInt();
        dimY = in.readInt();
        nMines = in.readInt();
        difficulty = in.readString();
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
        dest.writeString(difficulty);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameUtils> CREATOR = new Parcelable.Creator<GameUtils>() {
        @Override
        public GameUtils createFromParcel(Parcel in) {
            return new GameUtils(in);
        }

        @Override
        public GameUtils[] newArray(int size) {
            return new GameUtils[size];
        }
    };
}
