package com.example.morho.mytest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SeatActivity extends AppCompatActivity implements View.OnClickListener, AlertDialog.OnClickListener{
    private SeatImageView img;
    private GridLayout gridLayout;
    private boolean isBooked;
    private static final NetTool netTool = new NetTool();
    private App_DATA app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("图书馆座位预定");

        //this.img.getDrawable().setLevel(10000);
        //this.img.setMinimumWidth(img.getDrawable().getIntrinsicHeight());
        //Log.d("DEBUG", ""+ this.img.toString());
        this.gridLayout = (GridLayout) findViewById(R.id.grid);
        app = (App_DATA) getApplication();
        new SeatAsync().execute("GET_SEAT", "1", app.getName());
    }

    @Override
    public void onClick(View v) {
        SeatImageView seatImageView = (SeatImageView) v;
        Log.d("img debug", "view id is " + v.toString() + " seat id is " + Arrays.toString(seatImageView.getSeat()) + " !");
        this.img = seatImageView;
        if (this.img.isSeatSelected()) {
            this.showAlert(seatImageView.getSeat(), seatImageView.getNo());
        }
    }

    private void showAlert(int[] seatID, int tableNo) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        switch (seatID[0]) {
            case -1:
                alert.setMessage("The Seat " + (seatID[1] + 1) + " has been booked!");
                alert.setTitle("Seat Detail");
                alert.setPositiveButton("Yes", null);
                break;
            case 1:
                if (isBooked) {
                    alert.setMessage("You have booked seat!");
                    alert.setTitle("Seat Detail");
                    alert.setPositiveButton("Yes", null);
                    break;
                }
                alert.setMessage("Are you sure to book the Table " + (tableNo + 1) + " seat " + (seatID[1] + 1) + " !");
                alert.setTitle("Book insure");
                alert.setPositiveButton("Yes", this);
                alert.setNegativeButton("No", null);
                break;
            default:
                return;
        }
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:

                String location = "1";
                String table = this.img.getNo() + "";
                String seat = this.img.getSeat()[1] + "";
                String name = app.getName();
                Log.e("NAME DEBUG", "name is " + name);
                new SeatAsync().execute("BOOK_SEAT", name, table, seat, location);
                break;
        }
    }

    public void bookSeat(HashMap<String, Object> data) {

            if (data.get("result").equals(true)) {
                Toast.makeText(this, "Book ok!", Toast.LENGTH_SHORT).show();
                isBooked = true;
                this.img.changeStatus();
            } else {
                Toast.makeText(this, "Book failed!", Toast.LENGTH_SHORT).show();
            }
    }

    public void initSeat(HashMap<String, Object> data) {
        int count = (int) data.get("count");
        int self_table = -1;
        int self_seat = -1;
        /*
         * Tips:
         * Gson 转换时将int类型自动转换成double，数组也被转换为List!!
         */
        List<List<Double>> list = (List<List<Double>>) data.get("seat_data");
        List<Double> self_list = (List<Double>) data.get("self_book");
        if (null != self_list) {
            this.isBooked = true;
            self_table = self_list.get(0).intValue();
            self_seat = self_list.get(1).intValue();
        }
        int[][] seatStatus = new int[count][4];
        for (int i = 0; i < count; i++) {
            Arrays.fill(seatStatus[i], 0);
        }
        try {
            for (List<Double> seatsNo : list) {
                seatStatus[seatsNo.get(0).intValue()][(int)seatsNo.get(1).intValue()] = 1;
            }
        } catch (NullPointerException ex) {
            Log.d("LIST DEBUG", "It's empty!");
        }

        for (int i = 0; i < count; i++) {
            ImageView view;
            if (i == self_table) {
                view = new SeatImageView(this, seatStatus[i], i, self_seat);
            } else {
                view = new SeatImageView(this, seatStatus[i], i);
            }
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            view.setBackgroundResource(R.drawable.seat_180);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = screenWidth / 3;
            int rawWidth = view.getBackground().getMinimumWidth();
            params.setMargins(0, 10, 0, 10);
            view.getBackground().setLevel(10000);
            view.setMinimumWidth(view.getBackground().getIntrinsicHeight());
            view.setClickable(true);
            view.setOnClickListener(this);
            gridLayout.addView(view, params);
        }
    }

    private class SeatAsync extends AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected void onPostExecute(HashMap<String, Object> data) {
            String action = (String) data.get("action");
            switch (action) {
                case "GET_SEAT": {
                    initSeat(data);
                    break;
                }
                case "BOOK_SEAT": {
                    bookSeat(data);
                    break;
                }
            }

        }

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            HashMap<String, Object> data = new HashMap<>();
            String action = params[0];
            data.put("action", action);
            switch (action) {
                case "GET_SEAT": {
                    try {
                        int count = netTool.getSeatCount(params[1]);
                        List<List<Double>> list = netTool.getSeatDetail(params[1], params[2]);
                        try {
                            if (list.get(list.size() - 1).size()== 3) {
                                data.put("self_book", list.remove(list.size() - 1));
                            }
                        } catch (NullPointerException ex) {
                            Log.e("LIST DEBUG", "NO self_book!");
                        }
                        data.put("count", count);
                        data.put("seat_data", list);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "BOOK_SEAT": {
                    try {
                        boolean flag = netTool.getBookStatus(Arrays.copyOfRange(params, 1, params.length));
                        data.put("result", flag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            return data;
        }
    }
}
