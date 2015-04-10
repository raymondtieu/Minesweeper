package com.raymondtieu.minesweeper.adapters;

import android.graphics.Point;

/**
 * Created by raymond on 2015-04-10.
 */
public class PositionPointAdapter {

    private int dimX;
    private int dimY;

    public PositionPointAdapter(int x, int y) {
        dimX = x;
        dimY = y;
    }

    public Point positionToPoint(int position) {
        int i = position / dimY;
        int j = position % dimY;

        return new Point(i, j);
    }

    public int pointToPosition(Point point) {
        return point.x * dimY + point.y;
    }
}
