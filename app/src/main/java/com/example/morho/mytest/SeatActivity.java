package com.example.morho.mytest;

import android.graphics.drawable.RotateDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class SeatActivity extends AppCompatActivity {
    private ImageView img;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        //this.img.getDrawable().setLevel(10000);
        //this.img.setMinimumWidth(img.getDrawable().getIntrinsicHeight());
        //Log.d("DEBUG", ""+ this.img.toString());
        this.gridLayout = (GridLayout) findViewById(R.id.grid);
        for (int i = 0; i < 20; i++) {
            ImageView view = new ImageView(this);
            view.setImageDrawable(getResources().getDrawable(R.drawable.seat_180));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(10, 10, 10, 10);
            view.getDrawable().setLevel(10000);
            view.setMinimumWidth(view.getDrawable().getIntrinsicHeight());
            gridLayout.addView(view, params);
        }

    }
}
