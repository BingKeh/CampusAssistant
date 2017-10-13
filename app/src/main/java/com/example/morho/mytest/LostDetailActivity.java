package com.example.morho.mytest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class LostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("失物招领");
        Intent intent = getIntent();
        int img_value = intent.getIntExtra("IMG_VALUE", 0);
        Lost_Item_Entity entity = (Lost_Item_Entity) intent.getSerializableExtra("data");
        TextView context = (TextView) findViewById(R.id.news_content);
        TextView usr = (TextView) findViewById(R.id.lost_title);
        context.setText(entity.getContext());
        usr.setText("用户: " + entity.getUsr_name());
        Log.e("Intent info", "the index is " + img_value);
        ImageView imageView = (ImageView) findViewById(R.id.lost_img);
        Glide.with(this).load(img_value).into(imageView);
    }

    public void do_ok(View view) {
        this.finish();
    }
}
