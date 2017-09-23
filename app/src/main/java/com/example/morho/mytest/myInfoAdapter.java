package com.example.morho.mytest;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Morho on 8/24/2017.
 */

public class myInfoAdapter extends BaseAdapter {
    private List<HashMap<String, String>> info_list;
    protected TypedArray array;
    protected Context context;
    protected int height;


    @Override
    public int getCount() {
        return this.info_list.size();
    }

    @Override
    public Object getItem(int position) {
        return info_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public myInfoAdapter(List<HashMap<String, String>> info_list, TypedArray array, Context context) {
        this.info_list = info_list;
        this.array = array;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.info_list_item, null);
            viewHolder = new myInfoAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text.setHint(info_list.get(position).get("name"));
        String text = info_list.get(position).get("text");
        if (text != null) {
            viewHolder.text.setText(text);
        } else {
            viewHolder.text .setText(null);
        }
        viewHolder.img.setImageDrawable(array.getDrawable(position));
        if (position == 1) {
            convertView.measure(0, 0);
            this.height = convertView.getHeight();
        }
        return convertView;
    }

    protected class ViewHolder {
        protected View rootView;
        protected EditText text;
        protected ImageView img;

        public ViewHolder(View view) {
            this.rootView = view;
            this.text = (EditText) rootView.findViewById(R.id.text_info);
            this.img = (ImageView) rootView.findViewById(R.id.img_info);
        }
    }

    public int getHeight() {
        return this.height;
    }


}
