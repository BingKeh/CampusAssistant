package com.example.morho.mytest;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
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
            gridLayout.addView(layout);
        }
    }
}
