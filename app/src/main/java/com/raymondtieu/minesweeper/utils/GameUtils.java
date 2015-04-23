package com.raymondtieu.minesweeper.utils;

import android.graphics.Point;

/**
 * Created by raymond on 2015-04-22.
 */
public class GameUtils {

    private int dimX, dimY;

    public GameUtils(int x, int y) {
        dimX = x;
        dimY = y;
    }

    public Point getPoint(int position) {
        int i = position / dimY;
        int j = position % dimY;

        return new Point(i, j);
    }

    public int getPosition(int x, int y) {
        return x * dimY + y;
    }

}
