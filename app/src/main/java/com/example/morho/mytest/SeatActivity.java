package com.example.morho.mytest;

import android.graphics.drawable.RotateDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SeatActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        this.img = (ImageView) findViewById(R.id.img);
        //this.img.getDrawable().setLevel(10000);
        //this.img.setMinimumWidth(img.getDrawable().getIntrinsicHeight());
        Log.d("DEBUG", ""+ this.img.toString());
    }
}
