package com.example.morho.mytest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LostHistoryActivity extends AppCompatActivity implements LostHistoryAdapter.LostListener {
    private RecyclerView recyclerView;
    private LostHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_history);
        this.recyclerView = (RecyclerView) findViewById(R.id.list_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("招领历史");
        List<Lost_Item_Entity> list = new ArrayList<>();
        this.adapter = new LostHistoryAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyDecoration(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//            float time;
//            boolean down = false;
//            boolean isMove = false;
//            boolean isSingleTap = false;
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                final int ACTION = e.getAction();
//                final float x = e.getX();
//                final float y = e.getY();
//                View v = rv.findChildViewUnder(e.getX(),e.getY());
//                switch (ACTION) {
//                    case MotionEvent.ACTION_DOWN: {
//                        time = new Date().getTime();
//                        down = true;
//                        isMove = false;
//                        break;
//                    }
//
//                    case MotionEvent.ACTION_MOVE: {
//                        isMove = true;
//                        break;
//                    }
//
//                    case MotionEvent.ACTION_UP: {
//                        Log.e("Touch info", "ACTION UP");
//                        float up_time = new Date().getTime();
//                        if (isMove) {
//                            break;
//                        }
//                        isSingleTap = true;
//                        break;
//                    }
//                }
//
//                if (isMove) {
//                    if (null != v) {
//                        float f_y = v.getY();
//                        LinearLayout layout = (LinearLayout) v.findViewById(R.id.content);
//                        View parentContent = layout.findViewById(R.id.lost_more_content);
//                        if (null != parentContent) {
//                            float y_y = parentContent.getY();
//                            TextView content  = (TextView) layout.findViewById(R.id.lost_content);
//                            if (content != null) {
//                                float x1 = content.getX();
//                                float x2 = x1 + content.getWidth();
//                                float y1 = f_y + y_y + content.getY();
//                                float y2 = y1 + content.getHeight();
//                                Log.e("Touch info", "x: " + x + " y: " + y + " x1: " + x1 + " x2: " + x2 + " y1: " + y1 + " y2: " + y2);
//                                if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
//                                    //content.onTouchEvent(e);
//                                    Log.e("Touch info", "the content is " + content.getText().toString());
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (isSingleTap) {
//                    if (v != null) {
//                        // v is the CardView
//                        float f_x = v.getX();
//                        float f_y = v.getY();
//                        Log.e("Touch info", "f_x: " + f_x + " f_y: " + f_y);
//                        int position = rv.getChildAdapterPosition(v);
//                        LinearLayout layout = (LinearLayout) v.findViewById(R.id.content);
//                        if ((int) v.getTag() == 0) {
//                            LayoutInflater inflater = getLayoutInflater();
//                            View childView = inflater.inflate(R.layout.lost_detail_more, null);
//                            layout.addView(childView);
//                            TextView content  = (TextView) layout.findViewById(R.id.lost_content);
//                            LostHistoryAdapter adapter = (LostHistoryAdapter) rv.getAdapter();
//                            content.setText(adapter.getItem(position).getContext());
//                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childView.getLayoutParams();
//                            int top = MyTool.dip2px(12, getResources());
//                            params.setMargins(0, top, 0, 0);
//                            v.setTag(1);
//                        } else {
//                            layout.removeViewAt(1);
//                            v.setTag(0);
//                        }
//                        isSingleTap = false;
//                    }
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
        SharedPreferences share_data = getSharedPreferences("data", Context.MODE_PRIVATE);
        String usr = share_data.getString("USR_NAME", null);
        String[] params = { usr };
        new lost_net().execute(params);
    }

    private void onPostGet(List<HashMap<String, String>> list) {
        Log.e("LostHistory debug", list + "");
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> data = list.get(i);
            String context = data.get("ps");
            Lost_Item_Entity entity = new Lost_Item_Entity();
            entity.initDefaultData(i);
            entity.setContext(context);
            entity.setTitle(data.get("title"));
            entity.setUsr_name(data.get("user_name"));
            entity.setDate(data.get("lost_date").split(" ")[0]);
            entity.setLost_type(data.get("cate"));
            String status = data.get("status");
            if (status.equals(0 + "")) {
                status = "有效";
            } else {
                status = "失效";
            }
            entity.setStatus(status);
            this.adapter.addItem(entity);
        }
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v, LostHistoryAdapter.ViewHolder holder, Lost_Item_Entity entity) {
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.content);
        if ((int) v.getTag() == 0) {

            LayoutInflater inflater = getLayoutInflater();
            View childView = inflater.inflate(R.layout.lost_detail_more, null);
            layout.addView(childView);
            TextView content  = (TextView) layout.findViewById(R.id.lost_content);
            content.setText(entity.getContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) childView.getLayoutParams();
            int top = MyTool.dip2px(12, getResources());
            params.setMargins(0, top, 0, 0);
            v.setTag(1);
        } else {
            layout.removeViewAt(1);
            v.setTag(0);
        }
        Log.e("holder info", "title is " + holder.title.getText().toString());
    }


    private class lost_net extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            onPostGet(hashMaps);
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            List<HashMap<String, String>> lost_list = null;
            try {
                Log.e("String array info", strings[0]);
                lost_list = new NetTool().get_lost_list(strings);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (lost_list != null) {
                return lost_list;
            }
            return null;
        }
    }



}
