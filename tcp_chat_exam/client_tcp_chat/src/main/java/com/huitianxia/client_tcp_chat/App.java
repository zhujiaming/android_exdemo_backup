package com.huitianxia.client_tcp_chat;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    public static String TAG = "zjm";

    public static void loge(String msg) {
        Log.e(TAG, msg==null?"":msg);
    }
    public static void logi(String msg) {
        Log.i(TAG, msg==null?"":msg);
    }
}