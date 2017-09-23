package com.example.morho.mytest;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TimetableActivity extends AppCompatActivity implements WeekSelectDialogFragment.postWeekChanged{

    protected List<CourseItem> list;
    protected GridLayout gridLayout;
    protected TextView[] textViews = new TextView[15];
    protected TextView[][] text_arary = new TextView[11][7];
    private TextView week_span;
    protected int row;
    protected int column;
    protected int clickHashCode = -1;
    protected TextView selectedText;
    private Spinner spinner;
    protected Date term_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("课程表");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse("2017-04-25");
                    getWeekCourse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        ScoredbHelper scoredbHelper = new ScoredbHelper(getBaseContext());
        this.list = Coursedb.get_cs_list(scoredbHelper.getReadableDatabase());
        final TextView t = (TextView) findViewById(R.id.span_Mon);
        this.week_span = t;
        final Context context = this.getBaseContext();
        final List<CourseItem> myList = this.list;
        this.gridLayout = (GridLayout) findViewById(R.id.course_grid);
        t.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("The width is " + t.getWidth());
                GridLayout gridLayout = (GridLayout) findViewById(R.id.course_grid);
                LayoutInflater layoutInflater = getLayoutInflater();
                TextView textView = (TextView) layoutInflater.inflate(R.layout.course_null_text, null);
                TextView[][] text_arary = new TextView[11][7];
                float scale = getResources().getDisplayMetrics().density;
                for (int i = 0; i < 11; i++) {
                    for (int j = 0; j < 7; j++) {
                        text_arary[i][j] = (TextView) layoutInflater.inflate(R.layout.course_null_text, null);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.rowSpec = GridLayout.spec(i);
                        params.columnSpec = GridLayout.spec(j + 1);
                        params.setGravity(Gravity.FILL);
                        params.height = MyTool.dip2px(60, scale);
                        params.width = t.getWidth();
                        gridLayout.addView(text_arary[i][j], params);
                        text_arary[i][j].setTag("" + i + "," + j);
                        text_arary[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (clickHashCode == v.hashCode()) {
                                    String str = (String) v.getTag();
                                    System.out.println("The i is " + str.split(",")[0] + " j is " + str.split(",")[1]);
                                    clickHashCode = -1;
                                    v.setBackground(getDrawable(R.drawable.course_null_shape));
                                    return;
                                } else {
                                    if (selectedText != null) {
                                        selectedText.setBackground(getDrawable(R.drawable.course_null_shape));

                                    }
                                    v.setBackground(getDrawable(R.drawable.new_course));
                                    clickHashCode = v.hashCode();
                                    selectedText = (TextView) v;
                                }
                            }
                        });
                    }
                }


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date  = simpleDateFormat.parse("2017-02-27");
                    term_date = date;
                    init_course(myList, date, gridLayout, t);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
//        getSupportActionBar().setCustomView(R.layout.course_select);
//        this.spinner = (Spinner) getSupportActionBar().getCustomView().findViewById(R.id.spinner);
//        String[] array = new String[25];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = "第" + i + "周";
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.course_menu_select:
                WeekSelectDialogFragment weekSelectDialogFragment = new WeekSelectDialogFragment();
                weekSelectDialogFragment.show(getSupportFragmentManager(), "select");
        }
        return super.onOptionsItemSelected(item);
    }


    private void init_course(List<CourseItem> list, Date date, GridLayout gridLayout, View t) throws ParseException {
        List<CourseItem> list_ret = CourseItem.getCourseThisWeek(list, date);
        System.out.println(list_ret);
        addCourse(list_ret, gridLayout, t);
    }

    private void addCourse(List<CourseItem> list, GridLayout gridLayout, View t) {
        float scale = getResources().getDisplayMetrics().density;
        LayoutInflater layoutInflater = getLayoutInflater();
        TypedArray typedArray = getResources().obtainTypedArray(R.array.course_shape);
        int i = 0;
        for (CourseItem courseItem : list) {

            int rowIndex = courseItem.getTurns_begin() - 1;
            int columnIndex = courseItem.getDay_of_week() - 1;
            if (columnIndex == 0) { columnIndex = 7; }
            int turns = courseItem.getTurns_end() - courseItem.getTurns_begin() + 1;
            int height = MyTool.dip2px(60 * turns - 2, scale);
            int width = t.getWidth() - MyTool.dip2px(2, scale);
            TextView new_course = (TextView) layoutInflater.inflate(R.layout.course_item, null);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(rowIndex, turns);
            params.columnSpec = GridLayout.spec(columnIndex, 1);
            params.setGravity(Gravity.FILL);
            params.height = height;
            params.width = width;
            params.setMargins(0, 0, MyTool.dip2px(2, scale), MyTool.dip2px(2, scale));
            new_course.setText(courseItem.getName() + "@" + courseItem.getLocation());
            new_course.setTextSize(12);
            new_course.setBackground(typedArray.getDrawable(i % 5));
            new_course.setTag(courseItem);
            textViews[i] = new_course;
            gridLayout.addView(new_course, params);
            i++;

            new_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Your course info is " + (CourseItem) v.getTag());
                }
            });
        }
    }

    public void getWeekCourse(Date date) throws ParseException {
        for (TextView textView : this.textViews) {
            this.gridLayout.removeView(textView);
        }
        init_course(this.list, date, this.gridLayout, this.week_span);

    }


    @Override
    public void do_week_changed(int week) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse("2017-02-27");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.WEEK_OF_YEAR, calendar.get(calendar.WEEK_OF_YEAR) + week - 1);
            getWeekCourse(calendar.getTime());
            getSupportActionBar().setTitle("课程表(第" + week + "周)");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
