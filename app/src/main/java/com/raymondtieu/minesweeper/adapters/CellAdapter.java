package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Board;
import com.raymondtieu.minesweeper.services.Game;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

import java.util.List;

/**
 * Created by raymond on 2015-04-04.
 */
public class CellAdapter extends RecyclerView.Adapter<CellHolder> {

    private LayoutInflater inflater;
    private Board board;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public CellAdapter(Context context, Board board) {
        inflater = LayoutInflater.from(context);
        this.board = board;
    }

    @Override
    public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.cell, parent, false);
        CellHolder holder = new CellHolder(view, this);

        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(final CellHolder holder, int position) {
        Coordinates c = convertPosition(position);

        if (board.isRevealed(c.i, c.j)) {
            holder.cell.setText("" + board.getNumMines(c.i, c.j));
        } else {
            holder.cell.setText("+");
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
