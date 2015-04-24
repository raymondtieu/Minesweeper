package com.raymondtieu.minesweeper.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-04-16.
 */
public class TimerImageView extends ImageView {

    private Context mContext;
    private Animation animation;

    public TimerImageView(Context context) {
        super(context);
        this.mContext = context;

        // disable hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public TimerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        // disable hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void onValueChanged(boolean over) {
        if (over) {
            // start animation
            animation = AnimationUtils.loadAnimation(mContext, R.anim.timer);

            setSVGImage(mContext, R.raw.timer_red);

            startAnimation(animation);

        } else {
            clearAnimation();
            setSVGImage(mContext, R.raw.timer);
        }
    }

    private void setSVGImage(Context context, int id) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.setImageDrawable(svg.createPictureDrawable());
    }
}
