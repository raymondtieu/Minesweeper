package com.raymondtieu.minesweeper.controllers;

import android.view.View;

import com.raymondtieu.minesweeper.layouts.FlagImageView;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

/**
 * Created by raymond on 2015-04-17.
 */
public class FlagController {

    private FlagImageView flag;
    private OnePlayerGame game;

    public FlagController(FlagImageView flag) {
        this.flag = flag;

        flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlag();
            }
        });
    }

    public void toggleFlag() {
        if (!game.isFinished())
            game.toggleFlag();
    }

    public void setGame(OnePlayerGame game) {
        this.game = game;
        //this.game.setFlagListener(flag);

        this.flag.onValueChanged(false);
    }
}
