package com.example.morho.mytest;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("小工具");
        GridLayout gridLayout = (GridLayout) findViewById(R.id.tools_grid);
        int parentWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        Log.e("GridView msg", "The width is " + parentWidth);
        String items[] = getResources().getStringArray(R.array.tool_items);
        TypedArray array = getResources().obtainTypedArray(R.array.tools_icon);
        for (int i = 0; i < 3; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.tools_item, null);
            TextView textView = (TextView) layout.findViewById(R.id.tool_text);
            textView.setText(items[i]);
            ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
            imageView.setImageDrawable(array.getDrawable(i));
            imageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(parentWidth / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
            layout.setTag(i);
            layout.setOnClickListener(this);
            gridLayout.addView(layout);
            if (i == 0) {
                layout.setBackground(getDrawable(R.color.grey_white));
            }
        }

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.lost_frag);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ScoreFragment scoreFragment = new ScoreFragment();
        transaction.add(R.id.lost_frag, scoreFragment);
        transaction.show(scoreFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        switch (index) {
            case 1: {
                //v.setBackground(getDrawable(R.color.colorPrimary));
                Intent intent = new Intent(this, LostActivity.class);
                startActivity(intent);
            }
        }
        Log.e("lost_click", "the index is " + index);
    }
}
