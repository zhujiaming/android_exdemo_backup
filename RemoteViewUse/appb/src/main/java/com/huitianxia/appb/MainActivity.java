package com.huitianxia.appb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {
    public final static String REMOTE_ACTION = "com.huitianxia.remoteviewuse.remote";
    public final static String REMOTE_ACTION_VIEW = "com.huitianxia.remoteviewuse.remote.view";

    private LinearLayout mRemoteViewsContent;
    private BroadcastReceiver mRemoteViewsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = intent.getParcelableExtra(REMOTE_ACTION_VIEW);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRemoteViewsContent = (LinearLayout) findViewById(R.id.remote_views_content);
        IntentFilter filter = new IntentFilter(REMOTE_ACTION);
        registerReceiver(mRemoteViewsReceiver, filter);
    }

    private void updateUI(RemoteViews remoteViews) {
        View view = remoteViews.apply(this, mRemoteViewsContent);
        mRemoteViewsContent.addView(view);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRemoteViewsReceiver);
        super.onDestroy();
    }
}
