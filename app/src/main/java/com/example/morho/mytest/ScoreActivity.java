package com.example.morho.mytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private ListView listView;
    private HashMap<String, String> data;
    private SimpleAdapter simpleAdapter;
    private MyScoreAdapter myScoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("SCORE");
        do_close_drawer();
        listView = (ListView) findViewById(R.id.score_list);
        myScoreAdapter = new MyScoreAdapter(this, getList(), R.layout.list_item,
                new String[] {Scoredb.ScoreEntry.COLUMN_NAME_SC_ID,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME,
                        Scoredb.ScoreEntry.COLUMN_NAME_SC_SC},
                new int[] {R.id.sc_id, R.id.sc_name, R.id.sc_sc});
        listView.setAdapter(myScoreAdapter);
        listView.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected List<HashMap<String, String>> getList() {
        HashMap<String, String> data = new HashMap<>();
        List<HashMap<String, String>> list = new ArrayList<>();
        ScoredbHelper scoredbHelper = new ScoredbHelper(getApplicationContext());
        list = Scoredb.get_list(scoredbHelper.getReadableDatabase());
        return list;
    }

    private void do_close_drawer() {

        Intent intent = new Intent(MainActivity.ACTION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("The Thread is " + Thread.currentThread());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.ACTION);
                intent.putExtra("ACTION", MainActivity.ACT_CLOSE_DRAWER);
                sendBroadcast(intent);
            }
        }).start();
    }
}
