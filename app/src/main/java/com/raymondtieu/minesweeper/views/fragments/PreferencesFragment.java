package com.raymondtieu.minesweeper.views.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.raymondtieu.minesweeper.R;


public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
