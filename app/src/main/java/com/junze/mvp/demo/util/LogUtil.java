package com.junze.mvp.demo.util;

import android.util.Log;



/**
 * @author 2018/6/20 9:40 / mengwei
 */
public class LogUtil {

    public static final String TAG= "LogUtil";

    public static void e(String message) {
        LogUtil.e(TAG, message);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }
}
