package com.example.morho.mytest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseFragment.OnCourseFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCourseFragmentInteractionListener mListener;

    protected List<CourseItem> list;
    protected TextView[] textViews = new TextView[15];
    protected TextView[][] text_arary = new TextView[11][7];
    private TextView week_span;
    protected int row;
    protected int column;
    protected int clickHashCode = -1;
    protected TextView selectedText;
    private Spinner spinner;
    protected Date term_date;
    protected MyGridView gridView, gridLayout;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View content = inflater.inflate(R.layout.fragment_course, container, false);
        FloatingActionButton fab = (FloatingActionButton) content.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse("2017-09-25");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        ScoredbHelper scoredbHelper = new ScoredbHelper(getContext());
        this.list = Coursedb.get_cs_list(scoredbHelper.getReadableDatabase());
        final TextView t = (TextView) content.findViewById(R.id.span_Mon);
        this.week_span = t;
        //final Context context = this.getBaseContext();
        final List<CourseItem> myList = this.list;
        t.post(new Runnable() {
            @Override
            public void run() {
                gridView = gridLayout = new MyGridView(getContext(), t.getWidth());
                System.out.println("The width is " + t.getWidth());
                //GridLayout gridLayout = (GridLayout) content.findViewById(R.id.course_grid);
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                TextView textView = (TextView) layoutInflater.inflate(R.layout.course_null_text, null);
                float scale = getResources().getDisplayMetrics().density;

                ScrollView scrollView = (ScrollView) content.findViewById(R.id.scroll_timetable);
                ScrollView.LayoutParams params = new ScrollView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                for (int i = 0; i < 11; i++) {
                    TextView turns_shape = (TextView) layoutInflater.inflate(R.layout.course_turn_text, null);
                    MyGridView.LayoutParams layoutParams = new MyGridView.LayoutParams();
                    layoutParams.height = MyTool.dip2px(60, scale);
                    layoutParams.width = MyTool.dip2px(24, scale);
                    layoutParams.rowSpec = MyGridView.spec(0 + i, 1);
                    layoutParams.columnSpec = MyGridView.spec(0, 1);
                    layoutParams.setGravity(Gravity.FILL);
                    turns_shape.setText("" + (i + 1));
                    if (i % 2 == 0) {
                        turns_shape.setBackgroundResource(R.color.white);
                    }
                    gridView.addView(turns_shape, layoutParams);
                }

                for (int i = 1; i <= 7; i++) {
                    TextView span_text = (TextView) layoutInflater.inflate(R.layout.course_null_text, null);
                    GridLayout.LayoutParams myparams = new GridLayout.LayoutParams();
                    //span_text.setText("Hello");
                    myparams.rowSpec = GridLayout.spec(0 , 1);
                    myparams.columnSpec = GridLayout.spec(i, 1);
                    myparams.height = MyTool.dip2px(60, scale);
                    myparams.width = t.getWidth();
                    myparams.setGravity(Gravity.FILL);
                    span_text.setBackground(null);
                    gridView.addView(span_text, myparams);
                }

                gridView.setColumnCount(8);
                gridView.setRowCount(11);

                gridView.invalidate();
                scrollView.addView(gridView);
//                for (int i = 0; i < 11; i++) {
//                    for (int j = 0; j < 7; j++) {
//                        text_arary[i][j] = (TextView) layoutInflater.inflate(R.layout.course_null_text, null);
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.rowSpec = GridLayout.spec(i);
//                        params.columnSpec = GridLayout.spec(j + 1);
//                        params.setGravity(Gravity.FILL);
//                        params.height = MyTool.dip2px(60, scale);
//                        params.width = t.getWidth();
//                        gridLayout.addView(text_arary[i][j], params);
//                        text_arary[i][j].setTag("" + i + "," + j);
//                        text_arary[i][j].setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (clickHashCode == v.hashCode()) {
//                                    String str = (String) v.getTag();
//                                    System.out.println("The i is " + str.split(",")[0] + " j is " + str.split(",")[1]);
//                                    clickHashCode = -1;
//                                    v.setBackground(getActivity().getDrawable(R.drawable.course_null_shape));
//                                    return;
//                                } else {
//                                    if (selectedText != null) {
//                                        selectedText.setBackground(getActivity().getDrawable(R.drawable.course_null_shape));
//
//                                    }
//                                    v.setBackground(getActivity().getDrawable(R.drawable.new_course));
//                                    clickHashCode = v.hashCode();
//                                    selectedText = (TextView) v;
//                                }
//                            }
//                        });
//                    }
//                }


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date  = simpleDateFormat.parse("2017-09-04");
                    term_date = date;
                    getWeekCourse(new Date(), true);
                    mListener.onFragmentInteraction(null);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        return content;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCourseFragmentInteractionListener) {
            mListener = (OnCourseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCourseFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCourseFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    private void init_course(List<CourseItem> list, Date date, GridLayout gridLayout, View t) throws ParseException {
        List<CourseItem> list_ret = CourseItem.getCourseThisWeek(list, date);
        //this.list = list_ret;
        System.out.println(list_ret);
        addCourse(list_ret, gridLayout, t);
    }

    private void addCourse(List<CourseItem> list, GridLayout gridLayout, View t) {
        float scale = getResources().getDisplayMetrics().density;
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
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

    public void getWeekCourse(Date date, boolean isNew) throws ParseException {
        if (!isNew) {
            for (TextView textView : this.textViews) {
                this.gridLayout.removeView(textView);
            }
            Log.e("textViews info", "it's not a new view");
        } else {
            Log.e("textViews info", "it's a new view");
        }
        init_course(this.list, date, this.gridLayout, this.week_span);
    }

    public void do_week_changed(int week) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse("2017-09-04");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(calendar.WEEK_OF_YEAR, calendar.get(calendar.WEEK_OF_YEAR) + week - 1);
            Log.e("week count info", "the week is " + week);
            getWeekCourse(calendar.getTime(), false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {

        Log.e("textViews info", "start debug!");

        super.onResume();

    }
}
