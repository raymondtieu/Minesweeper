package com.raymondtieu.minesweeper.controllers;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.raymondtieu.minesweeper.layouts.FixedGridLayoutManager;

/**
 * Created by raymond on 2015-04-17.
 */
public class FieldController {

    private RecyclerView recyclerView;
    private FrameLayout mineField;
    private int cellWidth;

    public FieldController(RecyclerView recyclerView, FrameLayout mineField) {
        this.recyclerView = recyclerView;
        this.mineField = mineField;
    }

    public void setGameParams(int cellWidth, int x, int y) {
        this.cellWidth = cellWidth;

        initViewSize(x, y);
        initManager(y);
    }

    public void initViewSize(int x, int y) {
        // set appropriate size for view containers
        recyclerView.getLayoutParams().height = x * cellWidth;
        recyclerView.getLayoutParams().width = y * cellWidth;

        mineField.getLayoutParams().height = x * cellWidth;
        mineField.getLayoutParams().width = y * cellWidth;
    }

    public void initManager(int columns) {
        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(columns);

        recyclerView.setLayoutManager(manager);
    }
}
