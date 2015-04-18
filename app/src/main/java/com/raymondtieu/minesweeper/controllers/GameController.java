package com.raymondtieu.minesweeper.controllers;

import android.graphics.Point;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

/**
 * Created by raymond on 2015-04-17.
 */
public class GameController {

    private TextView difficulty;

    private OnePlayerGame game;

    private PositionPointAdapter positionAdapter;
    private FieldAdapter fieldAdapter;

    public GameController(TextView difficulty) {
        this.difficulty = difficulty;
    }

    public void setGame(OnePlayerGame game) {
        this.game = game;

        switch(game.getField().getDimY()) {
            case 9 : difficulty.setText(R.string.easy_title); break;
            case 16: difficulty.setText(R.string.medium_title); break;
            case 30: difficulty.setText(R.string.hard_title); break;
        }
    }

    public Game.Status onClick(int position) {
        Point p = positionAdapter.positionToPoint(position);
        return game.onClick(p.x, p.y);
    }

    public boolean onLongClick(int position) {
        Point p = positionAdapter.positionToPoint(position);
        return game.onLongClick(p.x, p.y);
    }

    public void setPositionAdapter(PositionPointAdapter positionAdapter) {
        this.positionAdapter = positionAdapter;
        game.setPositionAdapter(positionAdapter);
    }

    public void setFieldAdapter(FieldAdapter fieldAdapter) {
        this.fieldAdapter = fieldAdapter;
        game.setFieldAdapter(fieldAdapter);
    }

    public boolean isGameStarted() {
        return game.isStarted();
    }
}
