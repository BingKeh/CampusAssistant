package com.example.morho.mytest;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Morhop on 10/24/2017.
 */

public class SeatImageView extends AppCompatImageView {
    private int touchState = -1;
    private Paint paint;
    private boolean isSelected = false;
    private final int tableNo;
    private int self_seat = -1;

    /*
    初始版本
     */

//    // 第一个圆心
//    int x_1 = 53;
//    int y_1 = 60;
//
//    // 第二个圆心
//    int x_2 = 219;
//    int y_2 = 60;
//    int x_3 = 53;
//    int y_3 = 172;
//    int x_4 = 219;
//    int y_4 = 172;

    int[][] array = new int[4][2];
    int[][] raw_array = new int[4][2];
    int[] seatStatus = new int[4];
    int length = MyTool.dip2px(20, getResources());
    /*
     * 座位状态：
     *
     * { 是否为新增， 座位号 }
     *
     */
    int selectedSeat[] = { -1, -1 };


    public SeatImageView(Context context, int[] Status, int tableNo) {
        super(context);
        this.paint = new Paint();
        array[0] = new int[] { MyTool.dip2px(98, getResources()), MyTool.dip2px(60, getResources()) };
        array[1] = new int[] { MyTool.dip2px(263, getResources()), MyTool.dip2px(60, getResources()) };
        array[2] = new int[] { MyTool.dip2px(98, getResources()), MyTool.dip2px(173, getResources()) };
        array[3] = new int[] { MyTool.dip2px(263, getResources()), MyTool.dip2px(173, getResources()) };


        raw_array[0] = new int[] { MyTool.px2dip(array[0][0], getResources()), MyTool.px2dip(array[0][1], getResources()) };
        raw_array[1] = new int[] { MyTool.px2dip(array[1][0], getResources()), MyTool.px2dip(array[1][1], getResources()) };
        raw_array[2] = new int[] { MyTool.px2dip(array[2][0], getResources()), MyTool.px2dip(array[2][1], getResources()) };
        raw_array[3] = new int[] { MyTool.px2dip(array[3][0], getResources()), MyTool.px2dip(array[3][1], getResources()) };

        this.seatStatus = Arrays.copyOf(Status, Status.length);
        this.tableNo = tableNo;
        post(new Runnable() {
            @Override
            public void run() {
                initSeat();
            }
        });
    }

    public SeatImageView(Context context, int[] Status, int tableNo, int SeatNo) {
        this(context, Status, tableNo);
        this.self_seat = SeatNo;
    }

    public void initSeat() {
        setDrawingCacheEnabled(true);
        paint.setColor(getResources().getColor(R.color.green));
        paint.setStrokeWidth(5);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < 4; i++) {
            if (seatStatus[i] == 1) {
                canvas.drawCircle(raw_array[i][0], raw_array[i][1], length / 2, paint);
            }
        }

        if (self_seat != -1) {
            paint.setColor(getResources().getColor(R.color.colorPrimary));
            canvas.drawCircle(raw_array[self_seat][0], raw_array[self_seat][1], length / 2, paint);
        }
        setImageBitmap(bitmap);
    }

    public int[] getSeat() { return this.selectedSeat; }

    public void changeStatus() {
        this.seatStatus[selectedSeat[1]] = 1;
        self_seat = selectedSeat[1];
        initSeat();
        this.selectedSeat[0] = -1;
        this.selectedSeat[1] = -1;
    }

    public int getNo() { return this.tableNo; }

    public boolean isSeatSelected() { return this.isSelected; }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawWidth = getBackground().getMinimumWidth();
        float touchX = event.getX();
        float touchY = event.getY();


        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isSelected = false;
                touchState = 0;
            }
            case MotionEvent.ACTION_UP: {
                Log.e("SeatImageView DEBUG", "the position is " + touchX + ", " + touchY);

                if (touchState == 0) {
                    int seatId = -1;
                    for (int i = 0; i < 4; i++) {
                        if (Math.sqrt(Math.pow(touchX - raw_array[i][0], 2) + Math.pow(touchY - raw_array[i][1], 2)) <= length / 2 + 20) {
                            Log.e("TOUCH DEBUG", "It's " + (i + 1) + " !");
                            seatId = i + 1;
                            break;
                        }
                    }
//                    if (Math.sqrt(Math.pow(touchX - x_1, 2) + Math.pow(touchY - y_1, 2)) <= length / 2) {
//                        Log.e("TOUCH DEBUG", "It's 1!");
//                        seatId = 1;
//                    } else if (Math.sqrt(Math.pow(touchX - x_2, 2) + Math.pow(touchY - y_2, 2)) <= length / 2) {
//                        Log.e("TOUCH DEBUG", "It's 2!");
//                        seatId = 2;
//                    } else if (Math.sqrt(Math.pow(touchX - x_3, 2) + Math.pow(touchY - y_3, 2)) <= length / 2) {
//                        Log.e("TOUCH DEBUG", "It's 3!");
//                        seatId = 3;
//                    } else if (Math.sqrt(Math.pow(touchX - x_4, 2) + Math.pow(touchY - y_4, 2)) <= length / 2) {
//                        Log.e("TOUCH DEBUG", "It's 4!");
//                        seatId = 4;
//                    }

                    if (seatId != -1) {
                        setDrawingCacheEnabled(true);
                        paint.setColor(getResources().getColor(R.color.green));
                        paint.setStrokeWidth(5);
                        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawCircle(raw_array[seatId - 1][0], raw_array[seatId - 1][1], length / 2, paint);
//                        setImageBitmap(bitmap);
//                        Toast.makeText(this.getContext(), "You have choose the seat " + seatId + " !", Toast.LENGTH_SHORT).show();
                        this.selectedSeat[1] = seatId - 1;
                        this.selectedSeat[0] = this.seatStatus[seatId - 1] == 0 ? 1 : -1;
                        isSelected = true;
                    }

                    touchState = -1;
                }

            }
        }
        return super.onTouchEvent(event);
    }
}
