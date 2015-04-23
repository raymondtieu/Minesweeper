package com.raymondtieu.minesweeper.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.services.ViewListener;

/**
 * Created by raymond on 2015-04-16.
 */
public class FlagImageView extends ImageView {

    private Context mContext;

    public FlagImageView(Context context) {
        super(context);
        this.mContext = context;

        // disable hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public FlagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // disable hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void onValueChanged(boolean flag) {
        if (flag) {
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
