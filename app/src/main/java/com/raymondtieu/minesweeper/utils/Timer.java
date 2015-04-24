package com.raymondtieu.minesweeper.utils;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by raymond on 2015-04-23.
 */
public class Timer {
    private final TextView mTimerTextView;
    private Handler timerHandler;

    private long startTime = 0L;
    private long updatedTime = 0L;
    private long timeSwap = 0L;
    private long current = 0L;

    public Timer(TextView mTimerTextView) {
        timerHandler = new Handler();
        
        this.mTimerTextView = mTimerTextView;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long t) {
        updatedTime = t;
        timeSwap = t;

        updateTextView();
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(updateTimerThread, 0);
    }

    public long stop() {
        // stop timer
        pause();

        // get total elapsed time
        return updatedTime;
    }

    public void pause() {
        timeSwap += current;
        timerHandler.removeCallbacks(updateTimerThread);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            current = System.currentTimeMillis() - startTime;
            updatedTime = timeSwap + current;

            updateTextView();

            timerHandler.postDelayed(this, 1000);
        }
    };

    private void updateTextView() {
        mTimerTextView.setText("" + (updatedTime / 1000));
    }
}
