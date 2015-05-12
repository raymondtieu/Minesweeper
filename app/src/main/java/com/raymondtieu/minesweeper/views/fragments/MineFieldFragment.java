package com.raymondtieu.minesweeper.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.views.activities.MainActivity;
import com.raymondtieu.minesweeper.views.adapters.FieldAdapter;
import com.raymondtieu.minesweeper.layouts.FixedGridLayoutManager;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.presenters.MineFieldPresenter;
import com.raymondtieu.minesweeper.presenters.MineFieldPresenterImpl;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.utils.Notification;
import com.raymondtieu.minesweeper.views.MineFieldView;

/**
 * Created by raymond on 2015-04-22.
 */
public class MineFieldFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, MineFieldView {

    private static final String TAG = "MINEFIELD";

    private MineFieldPresenter presenter;

    private RecyclerView mRecyclerView;
    private FieldAdapter mAdapter;
    private FrameLayout mFrameLayout;

    // read this from shared preferences
    private int cellDimen = 90;

    public static MineFieldFragment newInstance() {
        Bundle args = new Bundle();

        MineFieldFragment f = new MineFieldFragment();

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater
                .inflate(R.layout.fragment_minefield, container, false);

        // set views
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.minefield);
        mFrameLayout = (FrameLayout) layout.findViewById(R.id.minefield_container);

        initialize();

        return layout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(savedInstanceState);
    }


    private void initialize() {
        if (presenter == null)
            presenter = new MineFieldPresenterImpl(this);
    }

    @Override
    public void setMineFieldViewSize(int x, int y) {
        mRecyclerView.getLayoutParams().height = x * cellDimen;
        mRecyclerView.getLayoutParams().width = y * cellDimen;

        mFrameLayout.getLayoutParams().height = x * cellDimen;
        mFrameLayout.getLayoutParams().width = y * cellDimen;
    }

    @Override
    public void setUpMineField(Field field, GameUtils gameUtils) {
        mAdapter = new FieldAdapter(getActivity(), field, cellDimen);

        mAdapter.setPositionAdapter(gameUtils);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

        // the recycler view manager
        FixedGridLayoutManager manager = new FixedGridLayoutManager();
        manager.setTotalColumnCount(field.getDimY());

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateField(Notification type, int position) {
        mAdapter.notifyChange(position, type);
    }

    @Override
    public void onWin() {
        // check if fragment has been detached
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.win_title)
                    .setMessage(R.string.win_message)
                    .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            presenter.startNewGame();
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public void onLose() {
        // check if fragment has been detached
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.lost_title)
                    .setMessage(R.string.lost_message)
                    .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int button) {
                            presenter.startNewGame();
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return presenter.onLongClick(position);
    }

    @Override
    public void newGame() {
        /* temporary */
        MainActivity activity = (MainActivity) getActivity();
        activity.newGameHeader();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        // get current settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        Log.i(TAG, "Long press: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_long_press_key), true));
        Log.i(TAG, "Quick reveal: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_quick_reveal_key), true));
        Log.i(TAG, "Quick toggle: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_quick_toggle_key), true));
        Log.i(TAG, "Sound: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_sound_key), true));
        Log.i(TAG, "Vibration: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_vibration_key), true));
        Log.i(TAG, "Volume toggle: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_volume_toggle_key), true));
        Log.i(TAG, "Autosave: " + sharedPreferences.getBoolean(
                getResources().getString(R.string.preference_autosave_key), true));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);

        Log.i(TAG, "Save Instance State");
    }
}
