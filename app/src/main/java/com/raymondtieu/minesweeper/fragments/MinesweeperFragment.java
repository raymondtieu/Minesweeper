package com.raymondtieu.minesweeper.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raymondtieu.minesweeper.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MinesweeperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinesweeperFragment extends Fragment {

    public MinesweeperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_minesweeper, container, false);
    }


}
