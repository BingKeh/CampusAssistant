package com.example.morho.mytest;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Morho on 8/13/2017.
 */

public class Lost_Item_Entity implements Serializable{
    private String lost_type;
    private String Title;
    private String context;
    private int usr_id;
    private String date;
    private int img;
    private String usr_name;
    private String status;

    public int getLost_id() {
        return lost_id;
    }

    public void setLost_id(int lost_id) {
        this.lost_id = lost_id;
    }

    private int lost_id;




    public void initDefaultData(int index) {
        this.Title = "失物招领 " + index;
        this.context = "校园卡  掉落地点：图书馆   学号： 202140916";
        this.usr_id = 202140904;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.date = simpleDateFormat.format(new Date());
    }

    public String getLost_type() {
        return lost_type;
    }

    public void setLost_type(String lost_type) {
        this.lost_type = lost_type;
    }

    public String getTitle() {
        return Title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }
}
