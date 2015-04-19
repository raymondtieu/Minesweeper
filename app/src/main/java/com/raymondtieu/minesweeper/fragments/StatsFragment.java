package com.raymondtieu.minesweeper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Statistic;

/**
 * Created by raymond on 2015-04-18.
 */
public class StatsFragment extends Fragment {

    private static final String STATISTIC = "statistic";

    private TextView bestTime, gamesPlayed, gamesWon, winPercentage;
    private TextView winStreak, loseStreak, streak;

    public static StatsFragment getInstance(Statistic statistic) {
        StatsFragment statsFragment = new StatsFragment();

        Bundle args = new Bundle();

        args.putParcelable(STATISTIC, statistic);
        statsFragment.setArguments(args);

        return statsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stats, container, false);

        bestTime = (TextView) layout.findViewById(R.id.best_time);
        gamesPlayed = (TextView) layout.findViewById(R.id.games_played);
        gamesWon = (TextView) layout.findViewById(R.id.games_won);
        winPercentage = (TextView) layout.findViewById(R.id.win_perc);
        winStreak = (TextView) layout.findViewById(R.id.win_streak);
        loseStreak = (TextView) layout.findViewById(R.id.lose_streak);
        streak = (TextView) layout.findViewById(R.id.streak);

        Statistic s = null;
        Bundle bundle = getArguments();

        if (bundle != null)
            s = bundle.getParcelable(STATISTIC);

        if (s != null) {
            gamesPlayed.setText(String.valueOf(s.getGamesPlayed()));
            gamesWon.setText(String.valueOf(s.getGamesWon()));
        }

        return layout;
    }
}