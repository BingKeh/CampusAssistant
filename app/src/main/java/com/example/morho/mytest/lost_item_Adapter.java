package com.example.morho.mytest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Morho on 8/13/2017.
 */

public class lost_item_Adapter extends RecyclerView.Adapter<lost_item_Adapter.MyViewHolder> {
    private Context context;
    private List<Lost_Item_Entity> lostList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.card_title);
            content = (TextView) view.findViewById(R.id.news_content);
            img = (ImageView) view.findViewById(R.id.card_img);
        }
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Lost_Item_Entity lost = lostList.get(position);
        holder.title.setText(lost.getTitle());
        holder.content.setText(lost.getTitle());
        Glide.with(context).load(lost.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lostList.size();
    }

}
