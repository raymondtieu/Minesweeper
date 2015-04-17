package com.raymondtieu.minesweeper.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.raymondtieu.minesweeper.R;
import com.raymondtieu.minesweeper.models.NavBarData;

import java.util.Collections;
import java.util.List;

/**
 * Created by raymond on 2015-04-03.
 */
public class NavBarAdapter extends RecyclerView.Adapter<NavBarAdapter.NavItemHolder> {

    private LayoutInflater inflater;
    private List<NavBarData> titles = Collections.EMPTY_LIST;

    private Context mContext;

    private ClickListener clickListener;

    public NavBarAdapter(Context context, List<NavBarData> titles) {
        inflater = LayoutInflater.from(context);
        this.titles = titles;
        this.mContext = context;
    }

    @Override
    public NavItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // the root - linear layout
        View view = inflater.inflate(R.layout.drawer_item, parent, false);
        NavItemHolder holder = new NavItemHolder(view);

        // return holder that was inflated
        return holder;
    }

    @Override
    public void onBindViewHolder(NavItemHolder holder, int position) {
        NavBarData current = titles.get(position);
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(current.getIconId());
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class NavItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public NavItemHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);

    }
}