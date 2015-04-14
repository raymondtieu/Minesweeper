package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Cell;
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
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    private int cellDimensions;

    private PositionPointAdapter positionAdapter;

    HashMap<Integer, CellHolder> holders = new HashMap<>();

    Integer[] CELL_MINES = {
            R.raw.mine_1,
            R.raw.mine_2,
            R.raw.mine_3,
            R.raw.mine_4,
            R.raw.mine_5,
            R.raw.mine_6,
            R.raw.mine_7,
            R.raw.mine_8
    };

    Integer[] CELL_MINES_FILLED = {
            R.raw.fill_mine_1,
            R.raw.fill_mine_2,
            R.raw.fill_mine_3,
            R.raw.fill_mine_4,
            R.raw.fill_mine_5,
            R.raw.fill_mine_6,
            R.raw.fill_mine_7,
            R.raw.fill_mine_8
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

        Cell cell = field.getCell(p.x, p.y);
        Cell.Status status = cell.getStatus();

        // set cell to be a hidden cell
        holder.setBackground(mContext, R.raw.cell_bg);
        holder.mines.setImageResource(android.R.color.transparent);
        holder.icon.setImageResource(android.R.color.transparent);

        if (status == Cell.Status.REVEALED) {
            int n = cell.getAdjacentMines();

            if (n < 9) {
                holder.setIcon(mContext, R.raw.cell_outline);

                if (n > 0)
                    holder.setMines(mContext, CELL_MINES[n - 1]);
            } else {
                // set icon to be a mine
                holder.setIcon(mContext, R.raw.mine_red);
            }
        } else if (status == Cell.Status.FLAGGED) {
            holder.setMines(mContext, R.raw.flag_primary);
        } else if (status == Cell.Status.FLAG_CORRECT) {
            holder.setMines(mContext, R.raw.flag_correct);
        } else if (status == Cell.Status.FLAG_INCORRECT) {
            holder.setMines(mContext, R.raw.flag_incorrect);
        }
    }

    @Override
    public int getItemCount() {
        return field.getDimX() * field.getDimY();
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener i) {
        this.mOnItemClickListener = i;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener i) {
        this.mOnItemLongClickListener = i;
    }

    public void onCellClick(CellHolder holder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, holder.icon, holder.getPosition(),
                    holder.getItemId());
        }
    }

    public void onCellLongClick(CellHolder holder) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(null, holder.icon,
                    holder.getPosition(), holder.getItemId());
        }
    }

    public void setPositionAdapter(PositionPointAdapter adapter) {
        this.positionAdapter = adapter;
    }

    public void notifyRevealed(int position) {
        this.notifyItemChanged(position);

        CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);
        holder.mines.startAnimation(animation);
        holder.icon.startAnimation(animation);
    }

    public void notifyFlagged(final int position, final boolean isFlagged) {
        Animation animation;

        final FieldAdapter f = this;

        if (isFlagged)
            animation = AnimationUtils.loadAnimation(mContext, R.anim.shrink);
        else
            animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!isFlagged) {
                    f.notifyItemChanged(position);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFlagged) {
                    f.notifyItemChanged(position);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        CellHolder holder = holders.get(position);

        holder.mines.startAnimation(animation);

    }

    public void notifyMine(int position) {
        this.notifyItemChanged(position);

        CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.mine);
        holder.icon.startAnimation(animation);
    }

    public void notifyInvalid(int position) {
        CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.invalid);
        holder.icon.startAnimation(animation);
        holder.background.startAnimation(animation);
    }
}
