package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public ImageView mines;
    public ImageView fillMines;
    public ImageView icon;
    public ImageView background;

    public FieldAdapter mAdapter;

    public CellHolder(View itemView, FieldAdapter adapter, int size)  {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mAdapter = adapter;

        mines = (ImageView) itemView.findViewById(R.id.cell_mines);
        fillMines = (ImageView) itemView.findViewById(R.id.cell_fill);
        icon = (ImageView) itemView.findViewById(R.id.cell_icon);
        background = (ImageView) itemView.findViewById(R.id.cell_background);

        // disable hardware acceleration for image views
        mines.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        fillMines.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        background.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

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

    public void setMines(Context context, int minesId, int fillId) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), minesId);
        this.mines.setImageDrawable(svg.createPictureDrawable());

        if (fillId != -1) {
            SVG svgFill = SVGParser.getSVGFromResource(context.getResources(), fillId);
            this.fillMines.setImageDrawable(svgFill.createPictureDrawable());
        }
    }

    public void setIcon(Context context, int id) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.icon.setImageDrawable(svg.createPictureDrawable());
    }

    public void setBackground(Context context, int id) {
        SVG svg = SVGParser.getSVGFromResource(context.getResources(), id);

        this.background.setImageDrawable(svg.createPictureDrawable());
        this.fillMines.setImageDrawable((svg.createPictureDrawable()));
    }
}
