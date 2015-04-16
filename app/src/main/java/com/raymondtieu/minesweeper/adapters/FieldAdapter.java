package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Cell;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.services.Game;

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

        // set holder to be a hidden cell
        holder.setBackground(mContext, R.raw.cell_bg);
        holder.mines.setImageResource(android.R.color.transparent);
        holder.icon.setImageResource(android.R.color.transparent);

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

        if (status == Cell.Status.REVEALED) {
            int n = cell.getAdjacentMines();

            holder.mines.setImageResource(android.R.color.transparent);

            if (n < 9) {
                holder.setIcon(mContext, R.raw.cell_outline);

                if (n > 0)
                    holder.setMines(mContext, CELL_MINES[n - 1], CELL_MINES_FILLED[n - 1]);
            } else {
                // set icon to be a mine
                holder.setIcon(mContext, R.raw.mine_red);
            }
        } else if (status == Cell.Status.FLAGGED) {
            holder.setMines(mContext, R.raw.flag_primary, -1);
        } else if (status == Cell.Status.FLAG_CORRECT) {
            holder.setMines(mContext, R.raw.flag_correct, -1);
        } else if (status == Cell.Status.FLAG_INCORRECT) {
            holder.setMines(mContext, R.raw.flag_incorrect, -1);
        } else if (status == Cell.Status.HIDDEN) {
            holder.mines.setImageResource(android.R.color.transparent);
            holder.icon.setImageResource(android.R.color.transparent);
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


    public void notifyChange(int position, Game.Notification notification) {
        CellHolder cell = holders.get(position);

        if (notification == Game.Notification.REVEAL)
            notifyReveal(cell);
        else if (notification == Game.Notification.FLAG)
            notifyFlag(cell);
        else if (notification == Game.Notification.UNFLAG)
            notifyUnflag(cell);
        else if (notification == Game.Notification.INVALID_HIDDEN)
            notifyInvalidHidden(cell);
        else if (notification == Game.Notification.INVALID_REVEAL)
            notifyInvalidReveal(cell);
    }

    public void notifyReveal(final CellHolder cell) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cell.background.setImageResource(android.R.color.transparent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cell.mines.startAnimation(animation);
        cell.icon.startAnimation(animation);
    }

    public void notifyFlag(final CellHolder cell) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cell.mines.startAnimation(animation);
    }

    public void notifyUnflag(final CellHolder cell) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.shrink);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cell.mines.startAnimation(animation);
    }





    private void notifyRevealed(CellHolder cell) {
        this.notifyItemChanged(position);

        final CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                holder.background.setImageResource(android.R.color.transparent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        holder.mines.startAnimation(animation);
        holder.icon.startAnimation(animation);
    }

    private void notifyFlagged(final int position, final boolean isFlagged) {
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

    private void notifyMine(int position) {
        this.notifyItemChanged(position);

        CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.mine);
        holder.icon.startAnimation(animation);
    }

    public void notifyInvalid(int position) {
        final CellHolder holder = holders.get(position);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.invalid);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                holder.mines.setVisibility(View.INVISIBLE);
                holder.icon.setVisibility(View.INVISIBLE);
                holder.background.setVisibility(View.INVISIBLE);
                holder.fillMines.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                holder.mines.setVisibility(View.VISIBLE);
                holder.icon.setVisibility(View.VISIBLE);
                holder.background.setVisibility(View.VISIBLE);
                holder.fillMines.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        holder.fillMines.startAnimation(animation);
    }
}
