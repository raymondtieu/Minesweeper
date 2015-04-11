package com.raymondtieu.minesweeper.adapters;

import android.gesture.GestureOverlayView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView cell;
    public ImageView icon;

    public FieldAdapter mAdapter;

    public CellHolder(View itemView, FieldAdapter adapter, int size)  {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mAdapter = adapter;

        cell = (TextView) itemView.findViewById(R.id.cell_data);
        icon = (ImageView) itemView.findViewById(R.id.cell_icon);

        itemView.getLayoutParams().width = size;
        itemView.getLayoutParams().height = size;
    }

    @Override
    public void onClick(View v) {
        mAdapter.onCellClick(this);
    }

    @Override
    public boolean onLongClick(View v) {
        mAdapter.onCellLongClick(this);
        return true;
    }
}
