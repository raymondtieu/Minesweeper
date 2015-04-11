package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Field;

import java.util.HashMap;

/**
 * Created by raymond on 2015-04-04.
 */
public class FieldAdapter extends RecyclerView.Adapter<CellHolder> {

    private LayoutInflater inflater;
    private Field field;
    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    private int cellDimensions;

    private PositionPointAdapter positionAdapter;

    HashMap<Integer, CellHolder> holders = new HashMap<>();

    // 1 - blue, 2 - green, 3 - red, 4 - dark blue, 5 - dark red, 6 - teal
    // 7 - purple, 8 - black
    Integer[] MINE_COLOUR = {
            R.color.blue,
            R.color.green,
            R.color.red,
            R.color.darkblue,
            R.color.darkred,
            R.color.teal,
            R.color.purple,
            R.color.black
    };

    public FieldAdapter(Context context, Field field, int size) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.field = field;
        this.cellDimensions = size;
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.cell, parent, false);

        CellHolder holder = new CellHolder(view, this, cellDimensions);
        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(CellHolder holder, int position) {
        if (!holders.containsKey(position)) {
            holders.put(position, holder);
        }

        Point p = positionAdapter.positionToPoint(position);

        if (field.isRevealed(p.x, p.y)) {
            int n = field.getNumMines(p.x, p.y);
            if (n < 9) {
                if (n == 0) {
                    holder.cell.setText("");
                } else {
                    holder.cell.setText("" + n);
                    holder.cell.setTextColor(holder.cell
                            .getResources().getColor(MINE_COLOUR[n - 1]));

                }
                holder.icon.setImageResource(R.drawable.cell_bg);
            } else {
                holder.cell.setText("");
                holder.icon.setImageResource(R.drawable.mine);
            }
        } else if (field.isFlagged(p.x, p.y)) {
            holder.cell.setText("");
            holder.icon.setImageResource(R.drawable.flag);
        } else {
            holder.cell.setText("");
            holder.icon.setImageResource(R.drawable.hidden);
        }
    }

    @Override
    public int getItemCount() {
        return field.getDimX() * field.getDimY();
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener i) {
        this.mOnItemClickListener = i;
    }

    public void onCellClick(CellHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, holder.cell, holder.getPosition(),
                    holder.getItemId());
        }
    }

    public void setPositionAdapter(PositionPointAdapter adapter) {
        this.positionAdapter = adapter;
    }

    public void notifyRevealed(int position) {
        this.notifyItemChanged(position);

        CellHolder holder = holders.get(position);

        if (holder.hidden) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
            holder.cell.startAnimation(animation);
            holder.icon.startAnimation(animation);
            holder.hidden = false;
        }
    }

    public void notifyFlagged(int position) {
        this.notifyItemChanged(position);

        CellHolder holder = holders.get(position);

        if (holder.hidden) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.scale);
            holder.cell.startAnimation(animation);
            holder.icon.startAnimation(animation);
        }

    }
}
