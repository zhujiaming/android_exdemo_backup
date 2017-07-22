package com.huitianxia.tcpchatexam;

import android.app.Application;
import android.util.Log;

/**
 * Created by zhujiaming on 2017/7/18.
 */

public class App extends Application {
    public static String TAG = "zjm";

    public static void loge(String msg) {
        Log.e(TAG, msg==null?"":msg);
    }
    public static void logi(String msg) {
        Log.i(TAG, msg==null?"":msg);
    }
}
