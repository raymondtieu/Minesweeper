package com.raymondtieu.minesweeper.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.raymondtieu.minesweeper.views.HeaderView;

/**
 * Created by raymond on 2015-04-23.
 */
public class HeaderFragment extends Fragment implements HeaderView {

    private HeaderPresenter presenter;

    private TextView mMinesTextView;
    private ImageView mMinesImageView;

    private FlagImageView mFlagImageView;

    private TimerImageView mTimerImageView;
    private TextView mTimerTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater
                .inflate(R.layout.fragment_header, container, false);

        // set views
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

        // *** temp set icon for timer ***
        mTimerImageView.onValueChanged(false);
    }

    @Override
    public void updateMines(int numMines) {
        mMinesTextView.setText(String.valueOf(numMines));
    }

    @Override
    public void setFlag(boolean flag) {
        mFlagImageView.onValueChanged(flag);
    }
}
