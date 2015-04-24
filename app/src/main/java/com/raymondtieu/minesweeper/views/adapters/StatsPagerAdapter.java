package com.raymondtieu.minesweeper.views.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.views.fragments.StatsFragment;
import com.raymondtieu.minesweeper.models.Statistic;
import com.raymondtieu.minesweeper.services.DatabaseHandler;
import com.raymondtieu.minesweeper.utils.GameUtils;

/**
 * Created by raymond on 2015-04-18.
 */
public class StatsPagerAdapter extends FragmentPagerAdapter {

    private DatabaseHandler minesweeperDB;

    private String[] tabs;

    public StatsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        tabs = context.getResources().getStringArray(R.array.stats_tabs);
        minesweeperDB = new DatabaseHandler(context);
    }

    public void deleteAll() {
        minesweeperDB.deleteAll();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        Statistic s = null;

        if (position == 0)
            s = minesweeperDB.getStatistics(GameUtils.BEGINNER);
        else if (position == 1)
            s = minesweeperDB.getStatistics(GameUtils.INTERMEDIATE);
        else if (position == 2)
            s = minesweeperDB.getStatistics(GameUtils.ADVANCED);

        StatsFragment statsFragment = StatsFragment.getInstance(s);

        return statsFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
