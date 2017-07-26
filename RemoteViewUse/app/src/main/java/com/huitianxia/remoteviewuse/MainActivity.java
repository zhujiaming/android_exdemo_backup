package com.huitianxia.remoteviewuse;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 *  将一个view通过广播的方式传递给另一个应用（模拟应用发送通知过程）
 */

public class MainActivity extends AppCompatActivity {
    public final static String REMOTE_ACTION = "com.huitianxia.remoteviewuse.remote";
    public final static String REMOTE_ACTION_VIEW = "com.huitianxia.remoteviewuse.remote.view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("zjm", getPackageName());
        Log.e("zjm",         "layoutid:"+getResources().getIdentifier("layout_simulated_notification", "layout", "com.huitianxia.remoteviewuse")
        );
    }

    public void sendRemoteViews(View view) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_simulated_notification);
        remoteViews.setTextViewText(R.id.msg, "msg from process" + Process.myPid());

        Intent intent1 = new Intent();
        ComponentName componentName = new ComponentName("com.huitianxia.remoteviewuse","com.huitianxia.remoteviewuse.DemoActivity_1");
        intent1.setComponent(componentName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent2 = new Intent();
        ComponentName componentName2 = new ComponentName("com.huitianxia.remoteviewuse","com.huitianxia.remoteviewuse.DemoActivity_2");
        intent1.setComponent(componentName2);
        PendingIntent openActivity2PendingIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.item_holder, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.tv2, openActivity2PendingIntent);
//        remoteViews.setInt(R.id.tv2,"setGravity",5);

        Intent intent = new Intent(REMOTE_ACTION);
        intent.putExtra(REMOTE_ACTION_VIEW, remoteViews);
        sendBroadcast(intent);
    }
}
