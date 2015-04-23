package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Cell;
import com.raymondtieu.minesweeper.models.Field;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.utils.GameUtils;
import com.raymondtieu.minesweeper.utils.Notification;


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

    private GameUtils gameUtils;

    CellHolder[] holders;

    Integer[] CELL_MINES = {
            R.raw.mine_0,
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

        holders = new CellHolder[field.getDimX() * field.getDimY()];
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.cell, parent, false);

        CellHolder holder = new CellHolder(view, this, cellDimensions);

        // set holder to be a hidden cell
        holder.setBackground(mContext, R.raw.cell_bg);

        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(CellHolder holder, int position) {
        if (holders[position] == null) {
            holders[position] = holder;
        }

        Point p = gameUtils.getPoint(position);

        Cell cell = field.getCell(p.x, p.y);
        Cell.Status status = cell.getStatus();

        if (status == Cell.Status.REVEALED) {
            int n = cell.getAdjacentMines();

            if (n < 9) {
                if (n > 0)
                    holder.setIcon(mContext, CELL_MINES[n], CELL_MINES_FILLED[n - 1]);
                else
                    holder.setIcon(mContext, CELL_MINES[n], -1);
            } else {
                // set icon to be a mine
                holder.setIcon(mContext, R.raw.mine_red, -1);
            }
        } else if (status == Cell.Status.FLAGGED) {
            holder.setIcon(mContext, R.raw.flag_primary, -1);
        } else if (status == Cell.Status.FLAG_CORRECT) {
            holder.setIcon(mContext, R.raw.flag_correct, -1);
        } else if (status == Cell.Status.FLAG_INCORRECT) {
            holder.setIcon(mContext, R.raw.flag_incorrect, -1);
        } else if (status == Cell.Status.HIDDEN) {
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

    public void setPositionAdapter(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
    }


    public void notifyChange(int position, Notification notification) {
        if (notification == Notification.REVEAL)
            notifyReveal(position);
        else if (notification == Notification.FLAG)
            notifyFlag(position, true);
        else if (notification == Notification.UNFLAG)
            notifyFlag(position, false);
        else if (notification == Notification.MINE)
            notifyMine(position);
        else if (notification == Notification.INVALID_HIDDEN)
            notifyInvalid(position, true);
        else if (notification == Notification.INVALID_REVEAL)
            notifyInvalid(position, false);
    }

    private void notifyReveal(final int position) {
        final CellHolder cell = holders[position];

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

        this.notifyItemChanged(position);
        cell.icon.startAnimation(animation);
    }

    private void notifyFlag(final int position, final boolean flag) {
        CellHolder cell = holders[position];
        Animation animation;

        final FieldAdapter f = this;

        if (flag)
            animation = AnimationUtils.loadAnimation(mContext, R.anim.grow);
        else
            animation = AnimationUtils.loadAnimation(mContext, R.anim.shrink);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (flag)
                    f.notifyItemChanged(position);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!flag)
                    f.notifyItemChanged(position);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cell.icon.startAnimation(animation);
    }


    private void notifyMine(final int position) {
        final CellHolder cell = holders[position];

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.mine);

        this.notifyItemChanged(position);
        cell.icon.startAnimation(animation);
    }

    private void notifyInvalid(final int position, final boolean hidden) {
        final CellHolder cell = holders[position];

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.invalid);

        if (!hidden) {

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    cell.icon.setVisibility(View.INVISIBLE);
                    cell.toggleFlash(mContext, true);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    cell.icon.setVisibility(View.VISIBLE);
                    cell.toggleFlash(mContext, false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        this.notifyItemChanged(position);
        cell.background.startAnimation(animation);
    }
}