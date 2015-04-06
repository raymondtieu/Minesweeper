package com.raymondtieu.minesweeper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cell;
    public ImageView icon;

    public boolean hidden;

    public CellAdapter mAdapter;

    public CellHolder(View itemView, CellAdapter adapter, int size)  {
        super(itemView);
        itemView.setOnClickListener(this);

        mAdapter = adapter;

        cell = (TextView) itemView.findViewById(R.id.cell_data);
        icon = (ImageView) itemView.findViewById(R.id.cell_icon);

        hidden = true;

        itemView.getLayoutParams().width = size;
        itemView.getLayoutParams().height = size;
    }

    @Override
    public void onClick(View v) {
        mAdapter.onCellClick(this);
    }
}
