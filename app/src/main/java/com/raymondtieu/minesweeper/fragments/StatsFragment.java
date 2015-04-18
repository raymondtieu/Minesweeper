package com.raymondtieu.minesweeper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-04-18.
 */
public class StatsFragment extends Fragment {

    public static StatsFragment getInstance(int position) {
        StatsFragment statsFragment = new StatsFragment();

        Bundle args = new Bundle();

        args.putInt("pos", position);
        statsFragment.setArguments(args);

        return statsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stats, container, false);

        TextView tv = (TextView) layout.findViewById(R.id.stats_page);

        Bundle bundle = getArguments();
        if (bundle != null)
            tv.setText("This is page #" + bundle.getInt("pos"));

        return layout;
    }
}
