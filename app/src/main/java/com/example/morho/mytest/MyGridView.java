package com.example.morho.mytest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.GridLayout;

/**
 * Created by Morho on 8/21/2017.
 */

public class MyGridView extends GridLayout {
    private Paint paint;
    private int width;

    public MyGridView(Context context, int width) {
        super(context);
        this.paint = new Paint();
        this.width = width;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        float scale = getResources().getDisplayMetrics().density;
        System.out.println("The scale is " + scale);
        paint.setColor(getResources().getColor(R.color.base));
        paint.setAlpha(100);
        canvas.drawColor(Color.WHITE);
        for (int i = 1; i <= 11; i++) {
            canvas.drawLine(60, MyTool.dip2px(60 * i, scale), 1200, MyTool.dip2px(60 * i, scale), paint);
            //canvas.drawLine(60, MyTool.dip2px(60 * i, scale), 1200, MyTool.dip2px(60 * i, scale), paint);

        }

        for (int i = 0; i <= 6; i++) {
            //canvas.drawLine(MyTool.dip2px(24 + 48 * i, scale), 0, MyTool.dip2px(24 + 48 * i, scale), 3000, paint);
            //Log.e("MyGridView CLASS", "i get width " + width);
            canvas.drawLine(MyTool.dip2px(24 , scale) + width * i, 0, MyTool.dip2px(24 , scale) + width * i, 3000, paint);

        }

        super.dispatchDraw(canvas);
    }

    public MyGridView(Context context) {
        super(context);
    }
}
