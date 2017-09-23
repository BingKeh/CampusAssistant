package com.example.morho.mytest;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Morho on 8/6/2017.
 */

public class About_Adapter extends SimpleAdapter {
    private TypedArray array;


    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public About_Adapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                         int[] to) {
        super(context, data, resource, from, to);
    }

    public About_Adapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                         int[] to, TypedArray array) {
        super(context, data, resource, from, to);
        this.array = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        //TypedArray png_array = view.getResources().obtainTypedArray(R.array.about_png);
        ImageView img = (ImageView) view.findViewById(R.id.img_list);
        img.setImageDrawable(array.getDrawable(position));
        return view;
    }
}
