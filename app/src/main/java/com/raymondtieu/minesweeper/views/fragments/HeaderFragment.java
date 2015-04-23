package com.raymondtieu.minesweeper.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.layouts.MinesTextView;
import com.raymondtieu.minesweeper.presenters.HeaderPresenter;
import com.raymondtieu.minesweeper.presenters.HeaderPresenterImpl;
import com.raymondtieu.minesweeper.views.HeaderView;

/**
 * Created by raymond on 2015-04-23.
 */
public class HeaderFragment extends Fragment implements HeaderView {

    private HeaderPresenter presenter;

    private TextView mMinesTextView;
    // ....

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

        if (presenter == null)
            presenter = new HeaderPresenterImpl(this);

        return layout;
    }

    @Override
    public void updateMines(int numMines) {

    }

    @Override
    public void setFlag(boolean flag) {

    }
}
