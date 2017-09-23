package com.example.morho.mytest;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
    private static final NetTool netTool = new NetTool();
    protected EditText usr_text, pwd_text, re_pwd_text;
    private boolean usr_flag = false;
    private boolean pwd_flag = false;
    private boolean re_pwd_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.register_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.usr_text = (EditText) findViewById(R.id.usr_text);
        this.pwd_text = (EditText) findViewById(R.id.pwd_text);
        this.re_pwd_text = (EditText) findViewById(R.id.re_pwd_text);
        usr_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText text = (EditText) v;
                if (!hasFocus && !text.getText().toString().trim().equals("")) {
                    new get_reg().execute("DO_VALID", text.getText().toString());
                }
            }
        });
        re_pwd_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText text = (EditText) v;
                if (!hasFocus) {
                    if (!text.getText().toString().equals(pwd_text.getText().toString())) {
                        System.out.println("pwd is " + text.getText().toString() + " re_pwd is " +
                                pwd_text.getText().toString());
                        text.setError("密码不一致!");
                        re_pwd_flag = false;
                    } else {
                        System.out.println("pwd is " + text.getText().toString() + " re_pwd is " +
                                pwd_text.getText().toString());
                        re_pwd_flag = true;
                        pwd_flag = true;
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void do_register(View view) throws NoSuchAlgorithmException {
        String usr = usr_text.getText().toString();
        String pwd = pwd_text.getText().toString();
        MessageDigest md = MessageDigest.getInstance("md5");
        md.update(pwd.getBytes());
        String pwd_md5 = new BigInteger(1, md.digest()).toString(16);
        if (usr_text.getText().toString().trim().equals("")) {
            usr_flag = false;
            usr_text.setError("请输入用户名！");
            return;
        } else {
            usr_flag = true;
        }
        if (usr_flag && pwd_flag && re_pwd_flag) {
            new get_reg().execute("DO_REGISTER", usr, pwd_md5);
        }
    }

    public void post_valid(boolean flag) {
        System.out.println("The flag is " + flag);
        if (!flag) {
            usr_text.setError(getResources().getString(R.string.usr_error));
            usr_flag = false;
        } else {
            usr_flag = true;
        }
    }

    private void post_register(boolean flag) {
        if (!flag) {
            Snackbar.make(this.getCurrentFocus(), "注册失败！", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Toast.makeText(this.getApplicationContext(), "用户 " + usr_text.getText().toString() + " 注册成功", Toast.LENGTH_LONG);
            finish();
        }
    }



    private class get_reg extends AsyncTask<String, Integer, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(String... params) {
            HashMap<String, Object> data = new HashMap<>();
            String action = params[0];
            String usr = params[1];
            switch (action) {
                case "DO_VALID":
                    data.put("ACTION", "DO_VALID");
                    try {
                        boolean flag = netTool.do_valid(usr);
                        System.out.println("The flag is " + flag);
                        data.put("RESULT", flag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "DO_REGISTER":
                    data.put("ACTION", "DO_REGISTER");
                    String pwd_md5 = params[2];
                    try {
                        boolean flag = netTool.do_register(usr, pwd_md5);
                        data.put("RESULT", flag);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return data;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> data) {
            String action = (String) data.get("ACTION");
            System.out.println("The action is " + action);
            switch (action) {
                case "DO_VALID": {
                    boolean flag = (boolean) data.get("RESULT");
                    post_valid(flag);
                    break;
                }
                case "DO_REGISTER": {
                    boolean flag = (boolean) data.get("RESULT");
                    post_register(flag);
                    break;
                }

            }
        }
    }
}
