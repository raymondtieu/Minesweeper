package com.raymondtieu.minesweeper.views.adapters.viewholders;

import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
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

    private PictureDrawable cellImage;
    private PictureDrawable cellFillImage;

    private FieldAdapter mAdapter;

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

    public void clearIcon(int resId) {
        this.icon.setImageResource(resId);
    }

    public void setBackground(PictureDrawable cellBG) {
        this.background.setImageDrawable(cellBG);
    }

    public void setCellNum(PictureDrawable cellImage, PictureDrawable cellFillImage) {
        this.cellImage = cellImage;
        this.cellFillImage = cellFillImage;

        this.icon.setImageDrawable(this.cellImage);
    }

    public void setMine(PictureDrawable mineImage) {
        this.icon.setImageDrawable(mineImage);
    }

    public void setFlag(PictureDrawable flagImage) {
        this.icon.setImageDrawable(flagImage);
    }

    public void startInvalid() {
        if (cellFillImage != null) {
            this.icon.setVisibility(View.INVISIBLE);
            this.background.setImageDrawable(cellFillImage);
        }
    }

    public void stopInvalid() {
        this.icon.setVisibility(View.VISIBLE);
        this.background.setImageResource(android.R.color.transparent);
    }
}
