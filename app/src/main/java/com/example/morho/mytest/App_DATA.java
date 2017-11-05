package com.example.morho.mytest;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by Morhop on 8/8/2017.
 */

public class App_DATA extends Application {
    private int mHeight;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    @Override
    public void onCreate() {
        name = new String();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate();
    }
}
