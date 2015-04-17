package com.raymondtieu.minesweeper.controllers;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.raymondtieu.minesweeper.layouts.TimerImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by raymond on 2015-04-17.
 */
public class TimerController {

    private final TextView time;
    private final TimerImageView timerIcon;

    private Timer timer;
    private Handler timerHandler;

    private long startTime = 0L;
    private long updatedTime = 0L;
    private long timeSwap = 0L;
    private long current = 0L;

    public TimerController(TimerImageView timerIcon, TextView time) {
        this.timerIcon = timerIcon;
        this.time = time;

        timerHandler = new Handler();
    }

    public void init() {
        timerIcon.onValueChanged(0);
        time.setText("0");
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(updateTimerThread, 0);
    }

    public long stop() {
        // start icon animation
        timerIcon.onValueChanged(1);

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

            time.setText("" + (updatedTime / 1000));

            timerHandler.postDelayed(this, 1000);
        }
    };
}
