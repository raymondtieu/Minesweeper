package com.raymondtieu.minesweeper.utils;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-05-15.
 */
public class ImageLoader {

    private static final String TAG = "IMAGELOADER";

    private static final Integer CELL_BG = R.raw.cell_bg;
    private static final Integer MINE = R.raw.mine_red;
    private static final Integer FLAG_PRIMARY = R.raw.flag_primary;
    private static final Integer FLAG_GREEN = R.raw.flag_correct;
    private static final Integer FLAG_RED = R.raw.flag_incorrect;

    private Integer[] CELL = {
            R.raw.mine_0,
            R.raw.mine_1,
            R.raw.mine_2,
            R.raw.mine_3,
            R.raw.mine_4,
            R.raw.mine_5,
            R.raw.mine_6,
            R.raw.mine_7,
            R.raw.mine_8
    };

    private Integer[] CELL_FILL = {
            R.raw.fill_mine_1,
            R.raw.fill_mine_2,
            R.raw.fill_mine_3,
            R.raw.fill_mine_4,
            R.raw.fill_mine_5,
            R.raw.fill_mine_6,
            R.raw.fill_mine_7,
            R.raw.fill_mine_8
    };

    private static ImageLoader imageLoader;
    private Context context;

    private PictureDrawable[] cellImage, cellFillImage;
    private PictureDrawable cellBG, primaryFlagImage, redFlagImage,
            greenFlagImage, mineImage;

    protected ImageLoader(Context context) {
        this.context = context;
        cellImage = new PictureDrawable[CELL.length];
        cellFillImage = new PictureDrawable[CELL_FILL.length];
    }

    public static ImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            Log.i(TAG, "New instance");
            imageLoader = new ImageLoader(context);
        }

        return imageLoader;
    }

    public PictureDrawable getCellImage(int n) {
        if (cellImage[n] == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), CELL[n]);

            cellImage[n] = svg.createPictureDrawable();

            Log.i(TAG, "Loading cell image " + n + " for the first time");
        }

        return cellImage[n];
    }

    public PictureDrawable getCellFillImage(int n) {
        // n - 1 because there is no image for 0 mines
        if (cellFillImage[n - 1] == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), CELL_FILL[n - 1]);

            cellFillImage[n - 1] = svg.createPictureDrawable();

            Log.i(TAG, "Loading cell fill " + n + " for the first time");
        }

        return cellFillImage[n - 1];
    }

    public PictureDrawable getCellBG() {
        if (cellBG == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), CELL_BG);

            cellBG = svg.createPictureDrawable();

            Log.i(TAG, "Loading cell bg image for the first time");
        }

        return cellBG;
    }

    public PictureDrawable getMineImage() {
        if (mineImage == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), MINE);

            mineImage = svg.createPictureDrawable();

            Log.i(TAG, "Loading mine image for the first time");
        }

        return mineImage;
    }

    public PictureDrawable getPrimaryFlagImage() {
        if (primaryFlagImage == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), FLAG_PRIMARY);

            primaryFlagImage = svg.createPictureDrawable();

            Log.i(TAG, "Loading primary flag image for the first time");
        }

        return primaryFlagImage;
    }

    public PictureDrawable getGreenFlagImage() {
        if (greenFlagImage == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), FLAG_GREEN);

            greenFlagImage = svg.createPictureDrawable();

            Log.i(TAG, "Loading green flag image for the first time");
        }

        return greenFlagImage;
    }

    public PictureDrawable getRedFlagImage() {
        if (redFlagImage == null) {
            SVG svg = SVGParser.getSVGFromResource(context.getResources(), FLAG_RED);

            redFlagImage = svg.createPictureDrawable();

            Log.i(TAG, "Loading red flag image for the first time");
        }

        return redFlagImage;
    }
}
