package com.raymondtieu.minesweeper.views.adapters.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.views.adapters.FieldAdapter;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public ImageView icon;
    public ImageView background;

    private int fillId;

    public FieldAdapter mAdapter;

    public CellHolder(View itemView, FieldAdapter adapter, int size)  {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mAdapter = adapter;

        icon = (ImageView) itemView.findViewById(R.id.cell_icon);
        background = (ImageView) itemView.findViewById(R.id.cell_background);

        // disable hardware acceleration for image views
        icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        background.setLayerType(View.LAYER_TYPE_SOFTWARE, null);


        // blank cell
        icon.setImageResource(android.R.color.transparent);
        background.setImageResource(android.R.color.transparent);

        itemView.getLayoutParams().width = size;
        itemView.getLayoutParams().height = size;

        fillId = -1;
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

    public void setIcon(Context context, int id, int fillId) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.icon.setImageDrawable(svg.createPictureDrawable());

        this.fillId = fillId;
    }

    public void setBackground(Context context, int id) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.background.setImageDrawable(svg.createPictureDrawable());
    }

    public void toggleFlash(Context context, boolean fill) {
        if (fillId != -1) {
            if (fill) {
                SVG svg = SVGParser.getSVGFromResource(context.getResources(), fillId);

                this.background.setImageDrawable(svg.createPictureDrawable());
            } else {
                this.background.setImageResource(android.R.color.transparent);
            }
        }
    }
}
