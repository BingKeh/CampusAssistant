package com.example.morho.mytest;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

/**
 * Created by Morho on 7/29/2017.
 */

public class MyScoreAdapter extends SimpleAdapter {

    private List<HashMap<String, String>> list;
    private int postion = -1;
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
    public MyScoreAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.list = (List<HashMap<String, String>>) data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);

        int green = Color.rgb(0x00, 0xaa, 0x55);
        int red = Color.RED;
        int black = Color.BLACK;
        TextView text = (TextView) view.findViewById(R.id.sc_id);
        TextView score_text = (TextView) view.findViewById(R.id.sc_sc);
        //String score = ((TextView) view.findViewById(R.id.sc_sc)).getText().toString();
        String score = list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_SC);
        int sc = 60;
        try {
            if (score.contains(".")) {
                score = score.substring(0, score.lastIndexOf('.'));
            }
            sc = Integer.parseInt(score);
        } catch (NumberFormatException ex) {
            if (score.equals("优秀")) {
                sc = 90;
            } else if (score.equals("不及格")) {
                sc = 59;
            }
        }
        //System.out.println("The score is " + sc);
        if (sc >= 90) {
            score_text.setTextColor(green);
        } else if (sc < 60) {
            score_text.setTextColor(red);
        } else {
            score_text.setTextColor(black);
        }
        System.out.println("Postion is " + position + " name is " + text.getText().toString());
        return view;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }


}
