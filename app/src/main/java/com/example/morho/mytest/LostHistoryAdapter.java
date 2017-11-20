package com.example.morho.mytest;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morhop on 10/14/2017.
 */

public class LostHistoryAdapter extends RecyclerView.Adapter<LostHistoryAdapter.ViewHolder> {
    private List<Lost_Item_Entity> list;
    final LostListener mListener;


    public LostHistoryAdapter(List<Lost_Item_Entity> list, LostListener mListener) {
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lost_history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getDate());
        holder.category.setText(list.get(position).getLost_type());
        String status = list.get(position).getStatus();
        holder.status.setText(status);
        holder.setIsRecyclable(false);
        holder.itemView.setTag(0);
        if (status.equals("失效")) {
            holder.status.setTextColor(Color.RED);
        }
        if (status.equals("解决")) {
            holder.status.setTextColor(Color.BLUE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener instanceof LostListener) {
                    mListener.onClick(v, holder, list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView date;
        public final TextView title;
        public final TextView category;
        public final TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.text_date);
            title = (TextView) itemView.findViewById(R.id.text_title);
            category = (TextView) itemView.findViewById(R.id.text_cate);
            this.status = (TextView) itemView.findViewById(R.id.text_status);
        }
    }

    public void addItem(Lost_Item_Entity entity) {
        this.list.add(entity);
    }

    public Lost_Item_Entity getItem(int position) { return  this.list.get(position); }

    public void initList() { this.list = new ArrayList<>(); }

    protected interface LostListener {
        void onClick(View v, ViewHolder holder, Lost_Item_Entity entity);
    }
}
