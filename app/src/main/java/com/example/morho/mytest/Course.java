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

public class Course {
    private String c_name;
    private String teacher;
    private String location;
    private String type;
    private String time;

    private int day_of_week;
    private String week_type;
    private String[] begin_end;
    private String[] turns;



    public Course(String c_name,  String type, String time, String teacher,  String location) {
        super();
        this.c_name = c_name;
        this.teacher = teacher;
        this.location = location;
        this.type = type;
        this.time = time;
        getTimeInfo();
    }



    @Override
    public String toString() {
        return "\nCourse\n" + c_name + "\n" + type + "\n" + time + "\n" + teacher + "\n" + location + "\n";
    }



    public String getC_name() {
        return c_name;
    }



    public void setC_name(String c_name) {
        this.c_name = c_name;
    }



    public String getTeacher() {
        return teacher;
    }



    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }



    public String getLocation() {
        return location;
    }



    public void setLocation(String location) {
        this.location = location;
    }



    public String getType() {
        return type;
    }



    public void setType(String type) {
        this.type = type;
    }



    public String getTime() {
        return time;
    }



    public void setTime(String time) {
        this.time = time;
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

    public String[] getBegin_end() {
        return begin_end;
    }

    public void setBegin_end(String[] begin_end) {
        this.begin_end = begin_end;
    }

    public String[] getTurns() {
        return turns;
    }

    public void setTurns(String[] turns) {
        this.turns = turns;
    }

    public void getTimeInfo() {
        int index = time.indexOf("{");
        String week_info = time.substring(0, index);
        String week_turn = time.substring(index + 1, time.length() - 1);
        String[] array = week_turn.split("\\|");
        //String begin_end_str = array[0];
        String[] begin_end = array[0].substring(1, array[0].length() - 1).split("-");
        String week_type = "全";
        if (array.length > 1) {
            week_type = "" + array[1].charAt(0);
        }
        int day_of_week = -1;
        switch (week_info.charAt(1)) {
            case '一':
                day_of_week = Calendar.MONDAY;
                break;
            case '二':
                day_of_week = Calendar.TUESDAY;
                break;
            case '三':
                day_of_week = Calendar.WEDNESDAY;
                break;
            case '四':
                day_of_week = Calendar.THURSDAY;
                break;
            case '五':
                day_of_week = Calendar.FRIDAY;
                break;
        }
        String[] turns =  week_info.substring(week_info.indexOf('第') + 1, week_info.length() - 1).split(",");
        System.out.println(this.time);
        System.out.println("The day of week is " + getWeek(day_of_week) + " the turns is " + turns[0] + " to " + turns[1]);
        System.out.println("The begin week is " + begin_end[0] + " the end is " + begin_end[1] + ".\tThe week type is " + week_type);
        System.out.println("************************************");
        this.begin_end = begin_end;
        this.day_of_week = day_of_week;
        this.week_type = week_type;
        this.turns = turns;

    }

    public String getWeek(int day_of_week) {
        String week = "";
        switch (day_of_week) {
            case 2:
                week = "Monday";
                break;
            case 3:
                week = "Tuesday";
                break;
            case 4:
                week = "Wednesday";
                break;
            case 5:
                week = "Thursday";
                break;
            case 6:
                week = "Friday";
                break;
            default:
                break;
        }
        return week;
    }

    public boolean isToday(Date date) throws ParseException {
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
        int begin_week = Integer.parseInt(this.begin_end[0]);
        int end_week = Integer.parseInt(this.begin_end[1]);
        String type = now % 2 == 0 ? "双" : "单";
        if ((begin_week <= now && now <= end_week) && (this.week_type.equals("全") || this.week_type.equals(type)) && this.day_of_week == day_of_week) {
            return true;
        }
        return false;
    }

    public static List<Course> getCourseToday(List<Course> kcb, Date date) throws ParseException {

        List<Course> list = new ArrayList<Course>();
        for (Course course : kcb) {
            if (course.isToday(date)) {
                list.add(course);
            }
        }

        return list;

    }

}
