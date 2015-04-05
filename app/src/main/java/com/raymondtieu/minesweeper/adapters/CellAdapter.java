package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Board;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellAdapter extends RecyclerView.Adapter<CellHolder> {

    private LayoutInflater inflater;
    private Board board;
    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    private int cellDimensions = 0;

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

    public CellAdapter(Context context, Board board) {
        inflater = LayoutInflater.from(context);
        this.board = board;
        this.mContext = context;
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.cell, parent, false);


        // calculate cell size
        if (cellDimensions == 0) {
            DisplayMetrics display = mContext.getResources().getDisplayMetrics();
            float width = display.widthPixels;

            cellDimensions = Math.round(width / ((float) 12.5));
        }

        CellHolder holder = new CellHolder(view, this, cellDimensions);
        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(CellHolder holder, int position) {
        Coordinates c = convertPosition(position);

        if (board.isRevealed(c.i, c.j)) {
            int n = board.getNumMines(c.i, c.j);
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
        } else {
            holder.cell.setText("");
            holder.icon.setImageResource(R.drawable.hidden);
        }
    }

    @Override
    public int getItemCount() {
        return board.getxDimension() * board.getyDimension();
    }

    public Coordinates convertPosition(int position) {
        int i = position / board.getyDimension();
        int j = position % board.getyDimension();

        Coordinates c = new Coordinates(i, j);
        return c;
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

    public class Coordinates {
        public int i;
        public int j;

        public Coordinates(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
