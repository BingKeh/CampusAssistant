package com.example.morho.mytest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Morho on 8/14/2017.
 */

public class ScoreBaseAdapter extends BaseAdapter {
    protected List<HashMap<String, String>> list;
    private Context context;
    protected int[] viewStatus;

    public ScoreBaseAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;
        this.viewStatus = new int[getCount()];
    }

    public void changeList(List<HashMap<String, String>> list) { this.list = list; }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (viewStatus[position] == 1) {
            viewHolder.head.setVisibility(View.GONE);
            viewHolder.sc.setVisibility(View.GONE);
            viewHolder.menuLy.setVisibility(View.VISIBLE);
            viewHolder.big_sc.setVisibility(View.VISIBLE);
        } else {
            viewHolder.head.setVisibility(View.VISIBLE);
            viewHolder.menuLy.setVisibility(View.GONE);
            viewHolder.big_sc.setVisibility(View.GONE);
            viewHolder.sc.setVisibility(View.VISIBLE);
        }



        viewHolder.sc_id.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_ID));
        viewHolder.years.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS));
        viewHolder.term.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM));
        viewHolder.name.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME));
        viewHolder.sc.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_SC));
        viewHolder.credits.setText("学分:" + list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_CREDITS));
        viewHolder.nsc.setText("平时成绩:"  + list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_NORMAL_SC));
        viewHolder.esc.setText("期末成绩:" + list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_END_SC));
        viewHolder.big_sc.setText(list.get(position).get(Scoredb.ScoreEntry.COLUMN_NAME_SC_SC));
        viewHolder.rootView.setOnClickListener(new MyitemListener(position));

        try {
            int i = Integer.parseInt(viewHolder.big_sc.getText().toString());
        } catch (NumberFormatException exception) {
            viewHolder.big_sc.setTextSize(32);
        }

        TextView score_text = viewHolder.sc;
        int green = Color.rgb(0x00, 0xaa, 0x55);
        int red = Color.RED;
        int black = Color.BLACK;
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

        if (viewHolder.head.getVisibility() == View.GONE) {
            System.out.println(viewHolder.name.getText().toString() + " is hidden!");
        }

        return convertView;
    }

    public void clearSelect() { this.viewStatus = new int[getCount()]; }


    protected class ViewHolder {
        private boolean isSelected;
        View rootView;
        LinearLayout head, tail;
        TextView sc_id, years, term, name, sc, big_sc, nsc, esc, credits;
        FrameLayout menuLy;
        public ViewHolder(View view){

            rootView = view;
            sc_id = (TextView) view.findViewById(R.id.sc_id);
            term = (TextView) view.findViewById(R.id.sc_term);
            years = (TextView) view.findViewById(R.id.sc_years);
            name = (TextView) view.findViewById(R.id.sc_name);
            sc = (TextView) view.findViewById(R.id.sc_sc);
            head = (LinearLayout) view.findViewById(R.id.score_head);
            tail = (LinearLayout) view.findViewById(R.id.score_tail);
            menuLy = (FrameLayout) view.findViewById(R.id.score_more);
            big_sc = (TextView) view.findViewById(R.id.big_sc_sc);
            credits = (TextView) view.findViewById(R.id.sc_credit);
            nsc = (TextView) view.findViewById(R.id.normal_sc);
            esc = (TextView) view.findViewById(R.id.end_sc);
        }

        public boolean isSelected() { return this.isSelected; }

        public void setSelected(boolean flag) { this.isSelected = flag; }
    }

    private class MyitemListener implements View.OnClickListener {
        private int postion;
        int mHiddenViewHeight;
        private View v;

        public MyitemListener(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            this.v = v;
            App_DATA data = (App_DATA) context.getApplicationContext();
            ViewHolder viewHolder = (ViewHolder) v.getTag();

            if (viewHolder.menuLy.getVisibility() == View.GONE) {
                HashMap<String, String> hashMap = list.get(postion);
                hashMap.put("HEIGHT", String.valueOf(viewHolder.head.getHeight()));
                hashMap.put("STATUS", "open");
                mHiddenViewHeight = viewHolder.head.getHeight();
                viewStatus[postion] = 1;
                viewHolder.head.setVisibility(View.GONE);
                viewHolder.sc.setVisibility(View.GONE);
                viewHolder.big_sc.setVisibility(View.VISIBLE);
                animateOPen(viewHolder.menuLy);
                System.out.println("Close animation");
            } else {
                HashMap<String, String> hashMap = list.get(postion);
                int i = Integer.parseInt(hashMap.get("HEIGHT"));
                hashMap.put("STATUS", "hidden");
                mHiddenViewHeight = i;
                System.out.println("Open animation");
                viewStatus[postion] = 0;
                animateClose(viewHolder.menuLy);
                viewHolder.big_sc.setVisibility(View.GONE);
                viewHolder.sc.setVisibility(View.VISIBLE);
                viewHolder.head.setVisibility(View.VISIBLE);

            }
        }

            private void animateOPen(final View view) {
                v.setClickable(false);
                view.setVisibility(View.VISIBLE);
                ValueAnimator valueAnimator = createDropAnimator(view, 0, mHiddenViewHeight);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setClickable(true);
                    }
                });
                valueAnimator.setInterpolator(new AccelerateInterpolator());
                valueAnimator.setDuration(500).start();
            }

            private void animateClose(final View view) {
                v.setClickable(false);
                ValueAnimator valueAnimator = createDropAnimator(view, mHiddenViewHeight, 0);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // TODO Auto-generated method stub
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        v.setClickable(true);

                    }
                });
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.setDuration(500).start();
            }

            private ValueAnimator createDropAnimator(final View view, int start, int end) {
                System.out.println("The Height is " + mHiddenViewHeight);
                ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // TODO Auto-generated method stub
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = (int) animation.getAnimatedValue();
                        view.setLayoutParams(params);
                    }
                });
                return valueAnimator;
            }

        }
}
