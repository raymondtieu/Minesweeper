package com.raymondtieu.minesweeper.controllers;

import android.content.Context;
import android.graphics.Point;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.adapters.PositionPointAdapter;
import com.raymondtieu.minesweeper.services.DatabaseHandler;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

import java.util.Date;

/**
 * Created by raymond on 2015-04-17.
 */
public class GameController {

    private TextView header;

    private OnePlayerGame game;

    private PositionPointAdapter positionAdapter;
    private FieldAdapter fieldAdapter;

    private DatabaseHandler minesweeperDB;

    public GameController(TextView header, Context context) {
        this.header = header;

        minesweeperDB = new DatabaseHandler(context);
    }

    public void setGame(OnePlayerGame game) {
        this.game = game;

    }

    public void onClick(int position) {
        Point p = positionAdapter.positionToPoint(position);
        game.onClick(p.x, p.y);
    }

    public boolean onLongClick(int position) {
        Point p = positionAdapter.positionToPoint(position);
        return game.onLongClick(p.x, p.y);
    }

    public void setPositionAdapter(PositionPointAdapter positionAdapter) {
        this.positionAdapter = positionAdapter;
      //  game.setPositionAdapter(positionAdapter);
    }

    public void setFieldAdapter(FieldAdapter fieldAdapter) {
        this.fieldAdapter = fieldAdapter;
       // game.setFieldAdapter(fieldAdapter);
    }

    public boolean isGameStarted() {
        return game.isStarted();
    }

    public boolean isGameFinished() {
        return game.isFinished();
    }

    public void addRecord(Long time) {
        Date date = new Date();

        // a null value for time means game was a loss
        minesweeperDB.addTime("beginner", date.getTime(), time);
    }
}
