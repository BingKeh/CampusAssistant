package com.example.morho.mytest;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsFragment.OnListFragmentInteractionListener,
        View.OnClickListener, CourseFragment.OnCourseFragmentInteractionListener, WeekSelectDialogFragment.postWeekChanged {

    private DrawerLayout drawer;
    private TextView usr_name;
    private TextView ins_name;
    private TextView aqi_text;
    private View header;
    private NavigationView navigationView;
    private boolean is_logged;
    private TextView test_text;
    public static final String ACTION = "broadcast.action";
    public static final String ACT_CLOSE_DRAWER = "CLOSE_DRAWER";
    private NewsFragment newsFragment;
    private ScoreFragment scoreFragment;
    private CourseFragment courseFragment;
    private ImageView img;
    private int[] fragmentStatus = { 0, 0, 0, 0};
    private android.support.v4.app.Fragment fragment[] = new android.support.v4.app.Fragment[5];
    private LinearLayout search_box;
    private Toolbar toolbar;
    private boolean isSearch, isExsiting;
    private EditText search_view;
    private long mExitTime;
    private Menu menu_score, menu_course;
    private boolean isCourseMenu = false;
    private boolean isScoreMenu = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        IntentFilter filter = new IntentFilter(LoginActivity.ACTION);
        registerReceiver(broadcastReceiver, filter);

        // The correct way to find widgets in NavigationView Header
        header = navigationView.getHeaderView(0);
        this.usr_name = (TextView) header.findViewById(R.id.nav_usr_text);
        this.ins_name = (TextView) header.findViewById(R.id.nav_ins_text);
        this.aqi_text = (TextView) header.findViewById(R.id.AQIText);
        this.img = (ImageView) header.findViewById(R.id.usr_img);
        this.search_box = (LinearLayout) this.findViewById(R.id.search_box_expanded);
        this.search_view = (EditText) findViewById(R.id.search_view);


        init_user();
        init_fragment();
        //showtest();
        new MainNet().execute("");

    }

    private void init_fragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        if (fragmentStatus[0] == 0) {
            courseFragment = new CourseFragment();
            fragment[0] = courseFragment;
            transaction.add(R.id.Main_Fragment, courseFragment);
        }
        checkFragment(courseFragment, transaction);
        transaction.commit();
        fragment[4] = courseFragment;

    }

    private void init_user() {
        SharedPreferences share_data = getSharedPreferences("data", Context.MODE_PRIVATE);
        if (share_data.getString("STATUS", null) == null || share_data.getString("STATUS", null) == "LOGOUT") {
            return;
        }
        if (share_data.getString("STATUS", null).equals("LOGGED")) {
                this.is_logged = true;
                System.out.println("It's true");
                usr_name.setText(share_data.getString("USR_NAME", null));
                ins_name.setText(share_data.getString("STU_ACADEMY", null));
                App_DATA data = (App_DATA) getApplication();
                data.setName(share_data.getString("USR_NAME", null));
            } else {
                this.is_logged = false;
                System.out.println("It's false");
        }

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("ACTION");
            System.out.println("The action is " + action);
            if (action.equals(LoginActivity.ACT_CLOSE_DRAWER)) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            } else if (action.equals(LoginActivity.DO_LOGIN)) {
                usr_name.setText(intent.getStringExtra("USR_NAME"));
                System.out.println(usr_name.getText().toString());
                is_logged = true;
            } else if (action.equals(My_infoActivity.DO_LOGIN_JWC)) {
                ins_name.setText(intent.getStringExtra("STU_ACADEMY"));
            }

        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println("clicked!");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (id) {

            case R.id.nav_course: {
                if (isScoreMenu) {
                    this.menu_course.clear();
                }
                if (!isCourseMenu) {
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.course_menu, this.menu_course);
                    this.isCourseMenu = true;
                }
                changeFragment(transaction, 0);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            }

            case R.id.nav_lab: {
                if (isCourseMenu) {
                    this.menu_course.clear();
                    isCourseMenu = false;
                }

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.score_menu, this.menu_course);
                this.isScoreMenu = true;
                changeFragment(transaction, 1);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            }


            case R.id.nav_news: {
                if (isCourseMenu || isScoreMenu) {
                    this.menu_course.clear();
                    isCourseMenu = false;
                }
                changeFragment(transaction, 2);

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            }


            case R.id.nav_tools: {
                if (isCourseMenu || isScoreMenu) {
                    this.menu_course.clear();
                    isCourseMenu = false;
                }
                changeFragment(transaction, 3);

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;

            }
            case R.id.nav_me:
                if (is_logged) {
                    do_my_info(findViewById(id));
//                    Snackbar.make(navigationView, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                } else {
                    do_login(findViewById(id));
                }
                return true;
            case R.id.nav_about:

                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;


        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(score_menu, menu);
        this.menu_course = menu;
        if (!isCourseMenu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.course_menu, this.menu_course);
            this.isCourseMenu = true;
        }
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        if (isSearch) {
            do_Search(!isSearch);
            return;
        }
        if (!isExsiting) {
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            isExsiting = true;
            return;
        } else if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            isExsiting = true;
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search_icon: {
                do_Search(true);
                break;
            }
            case R.id.score_search: {
                break;
            }
            case R.id.score_years: {
                break;
            }
            case R.id.course_menu_select: {
                WeekSelectDialogFragment weekSelectDialogFragment = new WeekSelectDialogFragment();
                weekSelectDialogFragment.show(getSupportFragmentManager(), "select");
                System.out.println("You have clicked week!");
                break;
            }
            case R.id.course_menu_term : {
                TermSelectDialogFragment termSelectDialogFragment = new TermSelectDialogFragment();
                termSelectDialogFragment.show(getSupportFragmentManager(), "TERM");
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void do_Search(boolean flag) {
        this.isSearch = flag;
        if (flag) {
            int cx = (search_box.getLeft() + search_box.getRight()) * 2 / 3;
            int cy = (search_box.getTop() + search_box.getBottom()) * 2 / 3;

            int finalRadius = Math.max(search_box.getWidth(), search_box.getHeight());

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(search_box, cx, cy, 0, finalRadius);

            toolbar.setVisibility(Toolbar.GONE);
            search_box.setVisibility(View.VISIBLE);
            anim.start();


            ImageButton button = (ImageButton) findViewById(R.id.search_back_button);
            button.setOnClickListener(this);
            ImageView close = (ImageView) findViewById(R.id.search_close_button);
            close.setOnClickListener(this);
            search_view.addTextChangedListener(new SearchWatcher());
            search_view.setFocusable(true);
            search_view.setFocusableInTouchMode(true);
            search_view.requestFocus();
            InputMethodManager inputManager = (InputMethodManager)search_view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(search_view, 0);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            search_box.setVisibility(View.GONE);
        }

    }

    private void do_about() {
        Intent intent = new Intent(this, AboutActivity.class);
        intent.putExtra("SAMPLE_MESSAGE", "test message");
        startActivity(intent);
    }

    public void do_setting(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("SAMPLE_MESSAGE", "test message");
        startActivity(intent);
    }

    public void do_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("SAMPLE_MESSAGE", "test message");
        startActivity(intent);
    }

    public void do_my_info(View view) {
        Intent intent = new Intent(this, My_infoActivity.class);
        intent.putExtra("SAMPLE_MESSAGE", "test message");
        startActivity(intent);
    }

    public void do_info(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("SAMPLE_MESSAGE", "test message");
        startActivity(intent);
        do_close_drawer();
    }

    public void do_login_info(View v) {
        if (is_logged) {
            do_info(v);
//                    Snackbar.make(navigationView, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
        } else {
            do_login(v);
        }
    }



    private void showtest() {
        ScoredbHelper scoredbHelper = new ScoredbHelper(getApplicationContext());
        Scoredb.get_list(scoredbHelper.getReadableDatabase());
    }

    public void do_close_drawer() {

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

    @Override
    protected void onResume() {
        isExsiting = false;
        init_user();
        System.out.println("OnResume ok!");
        super.onResume();
    }


    public void onListFragmentInteraction() {
        System.out.println("ToolFragment Interface Method!");

    }

    public void checkFragment(android.support.v4.app.Fragment fragment, FragmentTransaction transaction) {
        for (android.support.v4.app.Fragment f : this.fragment) {
            if (f == null || f.getClass().equals(fragment.getClass())) {
                continue;
            }
            transaction.hide(f);
            System.out.println("hide fragment " + f.getClass().toString());
        }
        transaction.show(fragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back_button: {
                System.out.println("It's back");
                do_Search(false);
                break;
            }
            case R.id.search_close_button: {
                System.out.println("It's close");
                if (search_view.getText().toString().equals("")) {
                    do_Search(false);
                } else {
                    search_view.setText("");
                }
                break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse("2017-09-04");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            int nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date);
            int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            Log.e("weeks count", "Now is " + nowWeek + " and start is " + startWeek);
            int week = nowWeek - startWeek + 1;
            getSupportActionBar().setTitle("课程表(第" + week + "周)");
            Log.e("interface invoke", "invoked!");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void do_week_changed(int week) {
        getSupportActionBar().setTitle("课程表(第" + week + "周)");
        this.courseFragment.do_week_changed(week);
    }

    private void changeFragment(FragmentTransaction transaction, int index) {
        if (this.fragment[4] != null && this.fragment[4] == this.fragment[index]) {
            ;
        } else {
            if (fragmentStatus[index] == 0) {
                switch(index) {
                    case 0:
                        this.fragment[index] = new CourseFragment();
                        break;
                    case 1:
                        this.fragment[index] = new ScoreFragment();
                        this.scoreFragment = (ScoreFragment) this.fragment[index];
                        break;
                    case 2:
                        this.fragment[index] = new NewsFragment();
                        this.newsFragment = (NewsFragment) this.fragment[index];
                        break;
                    case 3:
                        this.fragment[index] = new ToolFragment();
                        break;
                }
                transaction.add(R.id.Main_Fragment, this.fragment[index]);

                fragmentStatus[index] = 1;
            } else if (index == 2) {
                this.newsFragment.isRefreshing(true);
            }
            checkFragment(this.fragment[index], transaction);
            transaction.commit();
            this.fragment[4] = this.fragment[index];
        }
    }

    public void initAQI(String aqi) {
        Log.d("AQI DEBUG", "the aqi is " + aqi);
        this.aqi_text.setText(aqi_text.getText() + aqi);
    }

    private class MainNet extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            initAQI(s);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HashMap<String, String> data = new NetTool().getLastPM();
                String value = data.get("value");
                return value;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.println("The Text is " + s.toString());
            scoreFragment.search_lsit(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
