package com.example.morho.mytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AboutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView list_about, list_coder;
    private ArrayList<String> list_item, list_coder_item;
    private List<HashMap<String, String>> list_head, list_tail;
    private int hashcode[] = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("关于");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.list_about = (ListView) findViewById(R.id.list_about);
        this.list_coder = (ListView) findViewById(R.id.list_coder);
        list_item = new ArrayList<>();
        list_coder_item = new ArrayList<>();
        list_head = new ArrayList<>();
        list_tail = new ArrayList<>();
        String[] array = getResources().getStringArray(R.array.list_about);
        for (String str : array) {
            HashMap<String, String> data = new HashMap<>();
            data.put("data", str);
            list_head.add(data);
        }
        for (String str : getResources().getStringArray(R.array.list_coder)) {
            HashMap<String, String> data = new HashMap<>();
            data.put("data", str);
            list_tail.add(data);
        }
//        list_item.add(getResources().getString(R.string.about_version));
//        list_item.add(getResources().getString(R.string.about_suggestion));
//        list_item.add(getResources().getString(R.string.about_update));
//        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_item);
//        ArrayAdapter<String> myadapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_coder_item);

        //list_about.setAdapter(myadapter);

        TypedArray about_array = getResources().obtainTypedArray(R.array.about_png);
        TypedArray coder_array = getResources().obtainTypedArray(R.array.coder_png);

        About_Adapter adapter = new About_Adapter(this, list_head, R.layout.about_list_item,
                new String[] {"data"},
                new int[] {R.id.text_list},
                about_array
        );
        About_Adapter adapter2 = new About_Adapter(this, list_tail, R.layout.about_list_item,
                new String[] {"data"},
                new int[] {R.id.text_list},
                coder_array
        );
        list_about.setAdapter(adapter);
        list_coder.setAdapter(adapter2);
        list_about.setOnItemClickListener(this);
        list_coder.setOnItemClickListener(this);

        // hashcode of list_about and list_coder
        hashcode[0] = adapter.hashCode();
        hashcode[1] = adapter2.hashCode();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本号");
        builder.setMessage("\n当前版本号 V1.0.1");
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    private void showSugDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("建议反馈");
        builder.setMessage("\n请写下你的建议！");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("提交", null);
        builder.setView(R.layout.suggestion_text);
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int hashcode = parent.getAdapter().hashCode();
        if (hashcode == this.hashcode[0]) {
            // list_about clicked
            switch (position) {
                case 0:
                    showDialog();
                    break;
                case 1:

                    break;
                case 2:
                    //showSugDialog();
                    new SuggestionDialogFragment().show(getSupportFragmentManager(), "suggestion");
            }
        } else {
            // list_coder clicked
            switch (position) {
                case 0:
                case 1: {
//                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//                    editor.putString("STATUS", "LOGOUT");
//                    editor.commit();
                    Intent intent = new Intent(this, SeatActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
        // System.out.println("postion is " + position);
    }

    private class do_delay implements Runnable {

        @Override
        public void run() {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ACTIVITY INFO", "onPause invoked!");
    }

    private class do_click extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "SHOW_VER";
        }

        @Override
        protected void onPostExecute(String s) {
            showDialog();
        }
    }



}
