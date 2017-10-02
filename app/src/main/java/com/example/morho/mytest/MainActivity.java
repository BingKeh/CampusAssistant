package com.example.morho.mytest;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.morho.mytest.dummy.DummyContent;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.example.morho.mytest.R.menu.course_menu;
import static com.example.morho.mytest.R.menu.score_menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToolsFragment.OnListFragmentInteractionListener,
        View.OnClickListener, CourseFragment.OnCourseFragmentInteractionListener, WeekSelectDialogFragment.postWeekChanged {

    private DrawerLayout drawer;
    private TextView usr_name;
    private TextView ins_name;
    private View header;
    private NavigationView navigationView;
    private boolean is_logged;
    private TextView test_text;
    public static final String ACTION = "broadcast.action";
    public static final String ACT_CLOSE_DRAWER = "CLOSE_DRAWER";
    private ToolsFragment toolsFragment;
    private ScoreFragment scoreFragment;
    private CourseFragment courseFragment;
    private ImageView img;
    private int[] fragmentStatus = { 0, 0, 0};
    private android.support.v4.app.Fragment fragment[] = new android.support.v4.app.Fragment[4];
    private LinearLayout search_box;
    private Toolbar toolbar;
    private boolean isSearch, isExsiting;
    private EditText search_view;
    private long mExitTime;
    private Menu menu_score, menu_course;
    private boolean isCourseMenu = false;



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
        this.img = (ImageView) header.findViewById(R.id.usr_img);
        this.search_box = (LinearLayout) this.findViewById(R.id.search_box_expanded);
        this.search_view = (EditText) findViewById(R.id.search_view);
        init_user();
        init_fragment();
        //this.test_text = (TextView) this.findViewById(R.id.test_text);
        //showtest();
    }

    private void init_fragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        if (fragmentStatus[0] == 0) {
            courseFragment = new CourseFragment();
            fragment[2] = courseFragment;
            transaction.add(R.id.Main_Fragment, courseFragment);
        }
        checkFragment(courseFragment, transaction);
        transaction.commit();
        fragment[3] = courseFragment;
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

        switch (id) {
            case R.id.nav_course: {
                if (!isCourseMenu) {
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.course_menu, this.menu_course);
                    this.isCourseMenu = true;
                }
                if (fragment[3] != null && fragment[3] == courseFragment) {
                    ;
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    if (fragmentStatus[0] == 0) {
                        courseFragment = new CourseFragment();
                        fragment[0] = courseFragment;
                        transaction.add(R.id.Main_Fragment, courseFragment);
                    }
                    checkFragment(courseFragment, transaction);
                    transaction.commit();
                    fragment[3] = courseFragment;
                }

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            }
            case R.id.nav_tools: {
                if (isCourseMenu) {
                    this.menu_course.clear();
                    isCourseMenu = false;
                }
                if (fragment[3] != null && fragment[3] == toolsFragment) {
                    ;
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    if (fragmentStatus[2] == 0) {
                        toolsFragment = new ToolsFragment();
                        fragment[2] = toolsFragment;
                        transaction.add(R.id.Main_Fragment, toolsFragment);
                    }
                    checkFragment(toolsFragment, transaction);
                    transaction.commit();
                    fragment[3] = toolsFragment;
                }

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
                //do_about();

                Intent intent = new Intent(this, ToolsActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_lab: {
//                ScoredbHelper scoredbHelper = new ScoredbHelper(this);
//                List<CourseItem> list = Coursedb.get_cs_list(scoredbHelper.getReadableDatabase());
//                System.out.println( list + "The size of list is " + list.size());

//                Intent intent = new Intent(this, TimetableActivity.class);
//                intent.putExtra("SAMPLE_MESSAGE", "test message");
//                startActivity(intent);

                if (isCourseMenu) {
                    this.menu_course.clear();
                    isCourseMenu = false;
                }

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.score_menu, this.menu_course);
                this.isCourseMenu = true;

                if (fragment[3] != null && fragment[3] == scoreFragment) {
                    ;
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    if (fragmentStatus[2] == 0) {
                        scoreFragment = new ScoreFragment();
                        fragment[2] = scoreFragment;
                        transaction.add(R.id.Main_Fragment, scoreFragment);
                    }
                    checkFragment(scoreFragment, transaction);
                    transaction.commit();
                    fragment[3] = scoreFragment;
                }

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(score_menu, menu);
        this.menu_course = menu;
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
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.search_expand, null);
//        main_layout.addView(view);
        //toolbar_backup.setVisibility(Toolbar.VISIBLE);
//        toolbar.setVisibility(Toolbar.GONE);
//        search_box.setVisibility(View.VISIBLE);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));
//        getSupportActionBar().setCustomView(R.layout.search_expand);
//        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        ImageButton button = (ImageButton) toolbar.findViewById(R.id.search_back_button);
//        button.setOnClickListener(this);
//        ImageView close = (ImageView) toolbar.findViewById(R.id.search_close_button);
//        close.setOnClickListener(this);

    }

    public void checkFragment(android.support.v4.app.Fragment fragment, FragmentTransaction transaction) {
        for (android.support.v4.app.Fragment f : this.fragment) {
            if (f== null || f.getClass().equals(fragment.getClass())) {
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

    }


    @Override
    public void do_week_changed(int week) {
        getSupportActionBar().setTitle("课程表(第" + week + "周)");
        this.courseFragment.do_week_changed(week);
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
