package com.example.morho.mytest;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Morho on 8/13/2017.
 */

public class Lost_Item_Entity {
    private int lost_type;
    private String Title;
    private String context;
    private int usr_id;
    private Date data;
    private int img;




    public void initDefaultData(int index) {
        this.lost_type = 1;
        this.Title = "失物招领 " + index;
        this.context = "校园卡  掉落地点：图书馆   学号： 202140916";
        this.usr_id = 202140904;
        this.data = getData();
    }

    public int getLost_type() {
        return lost_type;
    }

    public void setLost_type(int lost_type) {
        this.lost_type = lost_type;
    }

    public String getTitle() {
        return Title;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
