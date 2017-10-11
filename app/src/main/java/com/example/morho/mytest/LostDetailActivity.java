package com.example.morho.mytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_detail);
        Intent intent = getIntent();
        int img_value = intent.getIntExtra("IMG_VALUE", 0);
        Log.e("Intent info", "the index is " + img_value);
        ImageView imageView = (ImageView) findViewById(R.id.lost_img);
        Glide.with(this).load(img_value).into(imageView);
    }
}
