package com.raymondtieu.minesweeper.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.services.ViewListener;

/**
 * Created by raymond on 2015-04-16.
 */
public class FlagImageView extends ImageView implements ViewListener {

    private Context mContext;

    public FlagImageView(Context context) {
        super(context);
        this.mContext = context;
    }

    public FlagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    @Override
    public void onValueChanged(int newValue) {
        if (newValue == 1) {
            setSVGImage(mContext, R.raw.flag_primary);
        } else {
            setSVGImage(mContext, R.raw.flag_deselect);
        }
    }

    private void setSVGImage(Context context, int id) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.setImageDrawable(svg.createPictureDrawable());
    }
}
