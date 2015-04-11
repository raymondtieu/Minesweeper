package com.raymondtieu.minesweeper.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.raymondtieu.minesweeper.models.Field;

/**
 * Created by raymond on 2015-04-11.
 */
public class MinesTextView extends TextView implements Field.MinesChangedListener {

    public MinesTextView(Context context) {
        super(context);
    }

    public MinesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onValueChanged(int newValue) {
        setText(String.valueOf(newValue));
    }
}
