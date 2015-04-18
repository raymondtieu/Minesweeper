package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.fragments.StatsFragment;

/**
 * Created by raymond on 2015-04-18.
 */
public class StatsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    String[] tabs;

    public StatsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.mContext = context;

        tabs = mContext.getResources().getStringArray(R.array.stats_tabs);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        StatsFragment statsFragment = StatsFragment.getInstance(position);

        return statsFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
