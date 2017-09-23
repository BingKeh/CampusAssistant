package com.example.morho.mytest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Morho on 8/19/2017.
 */

public class CourseItem {
    private String year;
    private String term;
    private String name;
    private String location;
    private String teacher;
    private int day_of_week;
    private String week_type;
    private int begin;
    private int end;
    private int turns_begin;
    private int turns_end;

    public CourseItem() { super(); }


    public CourseItem(String year, String term, String name, String location, String teacher,
                      int day_of_week, String week_type, int begin, int end, int turns_begin, int turns_end) {
        this.year = year;
        this.term = term;
        this.name = name;
        this.location = location;
        this.teacher = teacher;
        this.day_of_week = day_of_week;
        this.week_type = week_type;
        this.begin = begin;
        this.end = end;
        this.turns_begin = turns_begin;
        this.turns_end = turns_end;
    }

    @Override
    public String toString() {
        return "CourseItem{" +
                "year='" + year + '\'' +
                ", term='" + term + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", teacher='" + teacher + '\'' +
                ", day_of_week=" + day_of_week +
                ", week_type='" + week_type + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", turns_begin=" + turns_begin +
                ", turns_end=" + turns_end +
                "}\n";
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public String getWeek_type() {
        return week_type;
    }

    public void setWeek_type(String week_type) {
        this.week_type = week_type;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getTurns_begin() {
        return turns_begin;
    }

    public void setTurns_begin(int turns_begin) {
        this.turns_begin = turns_begin;
    }

    public int getTurns_end() {
        return turns_end;
    }

    public void setTurns_end(int turns_end) {
        this.turns_end = turns_end;
    }


    public boolean isToday(Date date, boolean isThisWeek) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date term_begin_date = simpleDateFormat.parse("2017-09-04");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(term_begin_date);
        int begin = calendar.get(calendar.WEEK_OF_YEAR);
        calendar.setTime(date);
        int end = calendar.get(Calendar.WEEK_OF_YEAR);
        int now = end - begin + 1;
        int day_of_week = calendar.get(calendar.DAY_OF_WEEK);
        int begin_week = this.begin;
        int end_week = this.end;
        String type = now % 2 == 0 ? "双" : "单";
        if ((begin_week <= now && now <= end_week) && (this.week_type.equals("全") || this.week_type.equals(type)) && (isThisWeek || this.day_of_week == day_of_week)) {
            return true;
        }
        return false;
    }

    public static List<CourseItem> getCourseToday(List<CourseItem> kcb, Date date) throws ParseException {
        List<CourseItem> list = new ArrayList<CourseItem>();
        for (CourseItem course : kcb) {
            if (course.isToday(date, false)) {
                list.add(course);
            }
        }
        return list;
    }

    public static List<CourseItem> getCourseThisWeek(List<CourseItem> kcb, Date date) throws ParseException {
        List<CourseItem> list = new ArrayList<CourseItem>();
        for (CourseItem course : kcb) {
            if (course.isToday(date, true)) {
                list.add(course);
            }
        }
        return list;
    }



}
