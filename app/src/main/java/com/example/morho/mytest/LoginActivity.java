package com.example.morho.mytest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.AndroidException;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_SUCCEED = 1;
    public static final String ACTION = "broadcast.action";
    public static final String ACT_CLOSE_DRAWER = "CLOSE_DRAWER";
    public static final String DO_LOGIN = "LOGIN";
    protected static final NetTool netTool = new NetTool();

    private EditText usr_text;
    private EditText pwd_text;
    private LinearLayout input_layout;
    private TextView register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        Intent intent = new Intent(ACTION);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("The Thread is " + Thread.currentThread());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Intent intent = new Intent(ACTION);
//                intent.putExtra("ACTION", ACT_CLOSE_DRAWER);
//                sendBroadcast(intent);
//            }
//        }).start();
        do_close_drawer();
        System.out.println("The Thread of Login is " + Thread.currentThread());
        //finish();
        this.usr_text = (EditText) findViewById(R.id.usr_text);
        this.pwd_text = (EditText) findViewById(R.id.pwd_text);
        this.input_layout = (LinearLayout) findViewById(R.id.input_layout);
        this.register = (TextView) findViewById(R.id.register_text);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        init_register();
    }

    private void init_register() {
        CharSequence text = register.getText();
        if (text instanceof Spannable) {
            int end  =text.length();
            Spannable sp = (Spannable) register.getText();
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new MyURLSpan(), 0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.GRAY), 0, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            register.setText(style);
        }
        //setContentView(register);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (ishidden(ev)) {

                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean ishidden(MotionEvent ev) {
        int[] p1 = {0, 0};
        int[] p2 = {0, 0};
        usr_text.getLocationInWindow(p1);
        pwd_text.getLocationInWindow(p2);
        int p1_bottom = p1[1] + usr_text.getHeight();
        int p2_bottom = p2[1] + pwd_text.getHeight();
        int p1_right = p1[0] + usr_text.getWidth();
        int p2_right = p2[0] + pwd_text.getWidth();
        if (ev.getX() > p1[0] && ev.getX() < p1_right && ev.getY() > p1[1] && ev.getY() < p1_bottom) {
            return false;
        } else if (ev.getX() > p2[0] && ev.getX() < p2_right && ev.getY() > p2[1] && ev.getY() < p2_bottom) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    public void do_test(View view) {
        Snackbar.make(input_layout, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void do_login(View view) {
        // login action
        String usr = usr_text.getText().toString();
        String pwd = pwd_text.getText().toString();
        new get_login().execute(usr, pwd);
    }

    private class MyURLSpan extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            int color = widget.getDrawingCacheBackgroundColor();
            widget.setBackgroundColor(Color.parseColor("#00aa55"));
            widget.setBackgroundColor(color);
            Intent intent = new Intent(LoginActivity.this, RegActivity.class);
            startActivity(intent);
            System.out.println("register clicked!");
        }
    }

    private void post_do_login(boolean flag) {
        if (flag) {
            Snackbar.make(input_layout, "Login Succeed!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            post_login();
        } else {
            Snackbar.make(input_layout, "Login Failed!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    private void do_close_drawer() {

        Intent intent = new Intent(ACTION);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("The Thread is " + Thread.currentThread());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ACTION);
                intent.putExtra("ACTION", ACT_CLOSE_DRAWER);
                sendBroadcast(intent);
            }
        }).start();
    }

    private void post_login() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("STATUS", "LOGGED");
        editor.putString("USR_NAME", usr_text.getText().toString());
        editor.commit();
        Intent intent = new Intent(ACTION);
        intent.putExtra("ACTION", DO_LOGIN);
        intent.putExtra("LOGIN_STATUS", LOGIN_SUCCEED);
        intent.putExtra("USR_NAME", usr_text.getText().toString());
        sendBroadcast(intent);
        finish();
    }


    private class get_login extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String usr = params[0];
            String pwd = params[1];
            boolean flag = false;
            try {
                MessageDigest md = MessageDigest.getInstance("md5");
                md.update(pwd.getBytes());
                String pwd_md5 = new BigInteger(1, md.digest()).toString(16);
                flag = netTool.do_login(usr, pwd_md5);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (flag) {
                return "OK";
            } else {
                return "FAILED";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            switch (s) {
                case "OK":
                    post_do_login(true);
                    break;
                case "FAILED":
                    post_do_login(false);
                    break;
            }
        }
    }
}
