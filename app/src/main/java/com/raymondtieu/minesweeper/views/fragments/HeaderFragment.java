package com.raymondtieu.minesweeper.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.layouts.FlagImageView;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.layouts.TimerImageView;
import com.raymondtieu.minesweeper.presenters.HeaderPresenter;
import com.raymondtieu.minesweeper.presenters.HeaderPresenterImpl;
import com.raymondtieu.minesweeper.services.Timer;
import com.raymondtieu.minesweeper.views.HeaderView;

/**
 * Created by raymond on 2015-04-23.
 */
public class HeaderFragment extends Fragment implements HeaderView {

    private static final String TAG = "HEADER";

    private HeaderPresenter presenter;

    private TextView mDifficulty;

    private TextView mMinesTextView;
    private ImageView mMinesImageView;

    private FlagImageView mFlagImageView;

    private TimerImageView mTimerImageView;
    private TextView mTimerTextView;

    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "Create view");

        // Inflate the layout for this fragment
        final View layout = inflater
                .inflate(R.layout.fragment_header, container, false);

        // set views
        mDifficulty = (TextView) layout.findViewById(R.id.difficulty);

        mMinesTextView = (TextView) layout.findViewById(R.id.mines);
        mMinesImageView = (ImageView) layout.findViewById(R.id.mines_icon);

        mFlagImageView = (FlagImageView) layout.findViewById(R.id.flag);

        mTimerTextView = (TextView) layout.findViewById(R.id.timer);
        mTimerImageView = (TimerImageView) layout.findViewById(R.id.timer_icon);

        initialize();

        return layout;
    }


    private void initialize() {
        if (presenter == null)
            presenter = new HeaderPresenterImpl(this);

        presenter.initialize();

        // initialize image views

        // disable hardware acceleration
        mMinesImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // set icon for mines
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.mine_toolbar);
        mMinesImageView.setImageDrawable(svg.createPictureDrawable());

        mFlagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onToggleFlag();
            }
        });
    }

    @Override
    public void setDifficulty(String difficulty) {
        mDifficulty.setText(difficulty.toUpperCase());
    }

    @Override
    public void updateMines(int numMines) {
        mMinesTextView.setText(String.valueOf(numMines));
    }

    @Override
    public void setFlag(boolean flag) {
        mFlagImageView.onValueChanged(flag);
    }

    @Override
    public void setTimer(Long time) {
        // initialize timer
        mTimerImageView.onValueChanged(false);
        timer = new Timer(mTimerTextView);

        timer.setUpdatedTime(time);
    }

    @Override
    public void startTimer() {
        timer.start();
    }

    @Override
    public void resumeTimer() {

    }

    @Override
    public void pauseTimer() {
        timer.pause();
    }

    @Override
    public Long stopTimer() {
        mTimerImageView.onValueChanged(true);

        return timer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        Log.i(TAG, "Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();

        Log.i(TAG, "Pause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG, "Save Instance State");
    }
}
