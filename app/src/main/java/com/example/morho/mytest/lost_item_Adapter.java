package com.example.morho.mytest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morho on 8/13/2017.
 */

public class lost_item_Adapter extends RecyclerView.Adapter<lost_item_Adapter.MyViewHolder> {
    private Context context;
    private List<Lost_Item_Entity> lostList;
    private onItemClickListener mOnClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;
        public ImageView img;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.card_title);
            content = (TextView) view.findViewById(R.id.news_content);
            img = (ImageView) view.findViewById(R.id.card_img);
            button = (Button) view.findViewById(R.id.more_btn);
        }
    }

    public interface onItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public void setOnClickListener(onItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }



    public lost_item_Adapter(Context context, List<Lost_Item_Entity> lostList) {
        this.context = context;
        this.lostList = lostList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lost_items, parent, false);
        return new MyViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (this.mOnClickListener != null) {

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Recycle view btn info", "button " + position + " clicked!");
                }
            });
            Log.e("Recycle View test", "is instance!");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnClickListener.onClick(v, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnClickListener.onLongClick(v, position);
                    return true;
                }
            });
        }

        Lost_Item_Entity lost = lostList.get(position);
        holder.title.setText(lost.getTitle());
        holder.content.setText(lost.getContext());
        Glide.with(context).load(lost.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lostList.size();
    }

    public List<Lost_Item_Entity> getList() {
        return this.lostList;
    }

    public void updateList(List<Lost_Item_Entity> list) {
        this.lostList = list;
    }

    public void addItem(int position, Lost_Item_Entity entity) {
        this.lostList.add(entity);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        this.lostList.remove(position);
        notifyItemRemoved(position);
    }

    public void resetList() {
        this.lostList = new ArrayList<Lost_Item_Entity>();
        notifyDataSetChanged();
    }


}
