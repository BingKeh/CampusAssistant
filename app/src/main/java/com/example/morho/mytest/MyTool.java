package com.example.morho.mytest;

import android.app.Activity;

/**
 * Created by Morho on 8/18/2017.
 */

public class MyTool {

    /**
     * dp转成px
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转成dp
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }
}
