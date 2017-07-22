package com.jm.asynctaskex1;

import android.os.AsyncTask;

/**
 * Created by zhujiaming on 17/7/17.
 */

public abstract class CusAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    public void publishProgressToTask(Progress... progress) {
        publishProgress(progress);
    }
}
