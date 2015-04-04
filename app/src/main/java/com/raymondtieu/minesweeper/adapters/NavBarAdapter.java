package com.raymondtieu.minesweeper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.NavBarData;

import java.util.Collections;
import java.util.List;

/**
 * Created by raymond on 2015-04-03.
 */
public class NavBarAdapter extends RecyclerView.Adapter<NavBarAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    private List<NavBarData> titles = Collections.EMPTY_LIST;

    public NavBarAdapter(Context context, List<NavBarData> titles) {
        inflater = LayoutInflater.from(context);
        this.titles = titles;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.item_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavBarData current = titles.get(position);
        holder.title.setText(current.getTitle());
        // holder.icon.setImageResource(current.getIconId());
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        // ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            // icon = (ImageView) itemView.findViewById(R.id.listIcon);

        }
    }
}