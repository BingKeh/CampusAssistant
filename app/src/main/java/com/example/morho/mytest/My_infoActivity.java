package com.example.morho.mytest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.style.ScaleXSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;

public class My_infoActivity extends AppCompatActivity implements GetCodeCallback {

    private LinearLayout code_layout;
    protected final NetTool netTool = new NetTool();
    private EditText usr_text;
    private EditText psd_text;
    private EditText code_text;
    private Button btn_get_info;
    private ProgressBar bar_wait;
    public static final String DO_LOGIN_JWC = "DO_LOGIN_JWC";
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Info");
        this.code_layout = (LinearLayout) findViewById(R.id.code_layout);
        this.usr_text = (EditText) findViewById(R.id.text_usr);
        this.psd_text = (EditText) findViewById(R.id.text_psd);
        this.code_text = (EditText) findViewById(R.id.text_code);
        this.btn_get_info = (Button) findViewById(R.id.btn_jwclogin);
        this.bar_wait = (ProgressBar) findViewById(R.id.progressBar_wait);
        this.scrollView = (ScrollView) findViewById(R.id.info_scroll);
        scrollView.smoothScrollTo(0, 0);
        do_close_drawer();
        init_net();
    }

    private void init_net() {
        String[] act = {"GET_CODE", "http://jwjx.njit.edu.cn/CheckCode.aspx"};
        new get_code().execute(act);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getCode() {

        return null;
    }

    public void do_code(View view) {
        Snackbar.make(view, "Refresh the code!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
    }

    public void do_login_jwc(View view) {
        String[] act = {"DO_LOGIN_JWC", usr_text.getText().toString(), psd_text.getText().toString(), code_text.getText().toString()};
        new get_code().execute(act);
        btn_get_info.setVisibility(btn_get_info.GONE);
        bar_wait.setVisibility(View.VISIBLE);
    }

    public void get_sc_info(final List<HashMap<String, String>> list) {
        final ScoredbHelper scoredbHelper = new ScoredbHelper(getApplicationContext());
        final List<HashMap<String, String>> real_list = list;
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        final HashMap<String, String> data_info = list.remove(0);
        editor.putString("STU_NAME", data_info.get("STU_NAME"));
        editor.putString("STU_ID", data_info.get("STU_ID"));
        editor.putString("STU_CLASS", data_info.get("STU_CLASS"));
        editor.putString("STU_ACADEMY", data_info.get("STU_ACADEMY"));
        editor.commit();
        data_info.put("STU_PWD", psd_text.getText().toString());
        SharedPreferences share_data = getSharedPreferences("data", Context.MODE_PRIVATE);
        data_info.put("USR_ID", share_data.getString("USR_NAME", null));
        Intent intent = new Intent(LoginActivity.ACTION);
        intent.putExtra("ACTION", DO_LOGIN_JWC);
        // intent.putExtra("LOGIN_STATUS", LOGIN_SUCCEED);
        intent.putExtra("STU_ACADEMY", data_info.get("stu_academy"));
        sendBroadcast(intent);
        new Thread(new Runnable()  {
            @Override
            public void run() {
                try {
                    netTool.do_upload_info(data_info);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SQLiteDatabase db = scoredbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                for (HashMap<String, String> data : real_list) {
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_YEARS, data.get("years"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_TERM, data.get("term"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_ID, Integer.parseInt(data.get("cid")));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_NAME, data.get("cname"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_SC, data.get("score"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_CREDITS, data.get("credits"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_NORMAL_SC, data.get("normal_score"));
                    values.put(Scoredb.ScoreEntry.COLUMN_NAME_SC_END_SC, data.get("end_score"));
                    System.out.println(data.get("cname"));
                    long newRowid = db.insert(Scoredb.ScoreEntry.TABLE_NAME, null, values);
                }

            }
        }).start();
//        for (HashMap<String, String> data : list) {
//            System.out.println(data.toString());
//        }
    }

    private void set_cs_info(final List<Course> list) {
        System.out.println(list);
        final ScoredbHelper scoredbHelper = new ScoredbHelper(getApplicationContext());
        new Thread(new Runnable()  {
            @Override
            public void run() {
                SQLiteDatabase db = scoredbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                for (Course data : list) {
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_YEAR, "2017-2018");
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_TERM, "1");
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_COURSE_NAME, data.getC_name());
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_TEACHER_NAME, data.getTeacher());
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_LOCATION, data.getLocation());
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_WEEK_TYPE, data.getWeek_type());
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_DAY_OF_WEEK, data.getDay_of_week());
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_TURNS_BEGIN, Integer.parseInt(data.getTurns()[0]));
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_TURNS_END, Integer.parseInt(data.getTurns()[1]));
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_BEGIN, Integer.parseInt(data.getBegin_end()[0]));
                    values.put(Coursedb.CourseEntity.COLUMN_NAME_END, Integer.parseInt(data.getBegin_end()[1]));
                    //System.out.println(data.get("cname"));
                    long newRowid = db.insert(Coursedb.CourseEntity.TABLE_NAME, null, values);
                }
            }
        }).start();
    }

    public void get_login_status(String name) {
        if (name != null) {
            My_infoActivity.this.btn_get_info.setFocusable(false);
            Snackbar.make(this.getCurrentFocus(), "用户" + name + "登录成功", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            bar_wait.setVisibility(View.GONE);
            btn_get_info.setText("拉取信息成功");
            btn_get_info.setVisibility(View.VISIBLE);
            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new do_delayed(), 3000);
        } else {
            Snackbar.make(this.getCurrentFocus(), "登录失败", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void showCode(Bitmap img) {
        code_layout.removeView((ProgressBar) findViewById(R.id.progressBar_code));
        ImageButton img_btn = (ImageButton) findViewById(R.id.img_btn_code);
        Drawable draw = new BitmapDrawable(img);
        img_btn.setBackground(draw);
        img_btn.setVisibility(ImageButton.VISIBLE);
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

    private class get_code extends AsyncTask<String, Integer, HashMap<String, Object>>{

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            String act = params[0];
            HashMap<String, Object> data = new HashMap<String, Object>();
            if (act.equals("GET_CODE")) {
                data.put("ACT", params[0]);
                System.out.println("Get code now...");
                String url = params[1];
                try {
                    Bitmap img = netTool.getCode(url);
                    data.put("img", img);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (act.equals("DO_LOGIN_JWC")) {
                System.out.println("do_login_jwc now ......");
                data.put("ACT", params[0]);
                String url = "http://jwjx.njit.edu.cn/Default2.aspx";
                String usr = params[1];
                String psd = params[2];
                String code = params[3];
                String name = null;
                boolean flag = false;
                List<HashMap<String, String>> list = null;
                try {
                    String[] Ret = netTool.do_login_jwc(url, usr, psd, code);
                    if (Ret[0].equals("SUCCEED")) {
                        name = Ret[1];

                    } else {
                        data.put("ERROR", "");
                        return data;
                    }
                    list = netTool.get_sc_info();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    data.put("LOGIN_STATUS", name);
                    data.put("sc_data", list);
                }
                return data;
            } else if (act.equals("DO_UPLOAD_INFO")) {

            } else if (act.equals("DO_GET_COURSE")) {
                data.put("ACT", params[0]);
                List<Course> list = null;
                try {
                    list = netTool.get_cs_info("");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    data.put("cs_data", list);
                }
                return data;
            }

            return null;

        }

        @Override
        protected void onPostExecute(HashMap<String, Object> data) {
            String act = (String) data.get("ACT");
            if (act.equals("GET_CODE")) {
                showCode((Bitmap) data.get("img"));
            } else if (act.equals("DO_LOGIN_JWC")) {
                List<HashMap<String, String>> list = (List<HashMap<String, String>>) data.get("sc_data");
                String name = (String) data.get("LOGIN_STATUS");
                get_login_status(name);
                get_sc_info(list);
                do_get_cs();
            } else if (act.equals("DO_GET_COURSE")) {
                List<Course> list = (List<Course>) data.get("cs_data");
                set_cs_info(list);
            }
        }
    }



    private void do_get_cs() {
        String[] act = {"DO_GET_COURSE"};
        new get_code().execute(act);
    }

    private class do_delayed implements  Runnable {

        @Override
        public void run() {
            My_infoActivity.this.finish();
        }
    }

}
