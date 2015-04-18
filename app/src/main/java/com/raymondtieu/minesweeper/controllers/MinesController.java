package com.raymondtieu.minesweeper.controllers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

/**
 * Created by raymond on 2015-04-17.
 */
public class MinesController {

    private final ImageView minesIcon;
    private MinesTextView mines;
    private OnePlayerGame game;

    public MinesController(ImageView minesIcon, MinesTextView mines, Context context) {
        this.minesIcon = minesIcon;
        this.mines = mines;

        setIcon(context);
    }

    private void setIcon(Context context) {
        // disable hardware acceleration
        this.minesIcon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        SVG svg = SVGParser.getSVGFromResource(context.getResources(), R.raw.mine_toolbar);
        minesIcon.setImageDrawable(svg.createPictureDrawable());
    }

    public void setGame(OnePlayerGame game) {
        this.game = game;

        this.game.setMinesListener(mines);
        this.mines.setText("" + game.getField().getMines());
    }
}
