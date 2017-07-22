package com.jm.asynctaskex1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaming on 17/7/16.
 */

public class MainHelper {

    private IMainView mainView;
    private List<Item> downloadItems;

    public MainHelper(IMainView mainView) {
        this.mainView = mainView;
        initTaskAndView();
    }

    private void initTaskAndView() {
        downloadItems = new ArrayList<>();
        addDatas();
        mainView.setListDatas(downloadItems);
    }

    private void addDatas() {

    }

    public void startAllTask() {
        //循环添加
        Item item;
        for (int i = 0; i < downloadItems.size(); i++) {
            item = downloadItems.get(i);
            item.task = formTask();
        }
        for (int i = 0; i < downloadItems.size(); i++) {
            item = downloadItems.get(i);
            item.task.execute();
        }
    }

    public void cancleAllTask() {

    }

    private CusAsyncTask formTask() {
        return new CusAsyncTask<String, Integer, Integer>() {
            @Override
            protected void onProgressUpdate(Integer... values) {

            }

            @Override
            protected Integer doInBackground(String... strings) {

                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {

            }

            @Override
            protected void onCancelled(Integer integer) {

            }

        };
    }

}
