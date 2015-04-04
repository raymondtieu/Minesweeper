package com.raymondtieu.minesweeper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.Board;
import com.raymondtieu.minesweeper.services.OnePlayerGame;

/**
 * Created by raymond on 2015-04-03.
 */
public class CellAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private Board board;

    public CellAdapter(Context context, Board board) {
        this.context = context;
        this.board = board;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cell;

        if (convertView == null) {
            inflater = LayoutInflater.from(context);
            cell = (View) inflater.inflate(R.layout.cell, null);
        } else {
            cell = (View) convertView;
        }

        final int i = position / board.getyDimension();
        final int j = position % board.getxDimension();

        TextView cell_data = (TextView) cell.findViewById(R.id.cell_data);

        if (board.isRevealed(i, j)) {
            cell_data.setText("" + board.getNumMines(i, j));
        } else {
            cell_data.setText("+");
        }
        return cell;
    }

    @Override
    public int getCount() {
        return board.getxDimension() * board.getyDimension();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
