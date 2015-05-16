package com.raymondtieu.minesweeper.views.adapters;

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
import com.raymondtieu.minesweeper.utils.ImageLoader;
import com.raymondtieu.minesweeper.views.adapters.viewholders.CellHolder;
import com.raymondtieu.minesweeper.models.Cell;
import com.raymondtieu.minesweeper.models.Field;
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

    private CellHolder[] holders;

    private ImageLoader imageLoader;

    public FieldAdapter(Context context, Field field, int size) {
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.field = field;
        this.cellDimensions = size;

        holders = new CellHolder[field.getDimX() * field.getDimY()];

        imageLoader = ImageLoader.getInstance(mContext);
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.cell, parent, false);

        CellHolder holder = new CellHolder(view, this, cellDimensions);

        // set holder to be a hidden cell
        holder.setBackground(imageLoader.getCellBG());

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
                if (n > 0) {
                    holder.setCellNum(imageLoader.getCellImage(n),
                            imageLoader.getCellFillImage(n));
                } else {
                    holder.setCellNum(imageLoader.getCellImage(0), null);
                }
            } else {
                // set icon to be a mine
                holder.setMine(imageLoader.getMineImage());

                // start animation for mines
                Animation animationMine = AnimationUtils.loadAnimation(mContext, R.anim.mine);
                holder.icon.startAnimation(animationMine);
            }
        } else if (status == Cell.Status.FLAGGED) {
            holder.setFlag(imageLoader.getPrimaryFlagImage());
        } else if (status == Cell.Status.FLAG_CORRECT) {
            holder.setFlag(imageLoader.getGreenFlagImage());
        } else if (status == Cell.Status.FLAG_INCORRECT) {
            holder.setFlag(imageLoader.getRedFlagImage());
        } else if (status == Cell.Status.HIDDEN) {
            holder.clearIcon(android.R.color.transparent);
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

    public void setGameUtils(GameUtils gameUtils) {
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

        this.notifyItemChanged(position);

        Animation animationGrow = AnimationUtils.loadAnimation(mContext, R.anim.grow);

        animationGrow.setAnimationListener(new Animation.AnimationListener() {
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

        cell.icon.startAnimation(animationGrow);
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
        this.notifyItemChanged(position);
    }

    private void notifyInvalid(final int position, final boolean hidden) {
        final CellHolder cell = holders[position];

        Animation animationInvalid = AnimationUtils.loadAnimation(mContext, R.anim.invalid);

        if (!hidden) {
            animationInvalid.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    cell.startInvalid();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    cell.stopInvalid();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        cell.background.startAnimation(animationInvalid);
    }
}