package com.jm.imageloadercustom.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.GridView;

import com.jm.imageloadercustom.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class GridViewActivity extends AppCompatActivity {

    private GridView gridView;
    private List<String> datas;
    private ImageAdapter adapter;
    private String[] urls = new String[]{
            "http://img.mmjpg.com/small/2017/1068.jpg",
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"



            ,
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"





            ,
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"





            ,
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"




            ,
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"





            ,
            "https://t1.onvshen.com:85/gallery/20763/23461/s/002.jpg",
            "http://img18.3lian.com/d/file/201707/30/small062442595ea32256462925712699b2ec1501376058.jpg",
            "http://img.mmjpg.com/small/2017/1067.jpg",
            "http://img.mmjpg.com/small/2017/1066.jpg",
            "http://img18.3lian.com/d/file/201707/22/smalla040a327e613e1a818a92a166c471d621500790185.jpg",
            "http://img.mmjpg.com/small/2017/1064.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/0.jpg",
            "https://t1.onvshen.com:85/gallery/21501/23353/s/001.jpg",
            "http://img.mmjpg.com/small/2017/1056.jpg",
            "http://img18.3lian.com/d/file/201707/13/small56c12eb4f01fb4948198104c7c4b84a41499935792.jpg",
            "https://t1.onvshen.com:85/gallery/24180/23538/s/003.jpg",
            "https://img.onvshen.com:85/gallery/20440/18974/028.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/001.jpg",
            "https://img.onvshen.com:85/gallery/18220/13124/019.jpg"

    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        init();
        initData();
        gridView.setAdapter(adapter);

    }

    private void initData() {
        datas.addAll(Arrays.asList(urls));
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.grid_view);
        datas = new ArrayList<>();
        adapter = new ImageAdapter(datas, this);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i("zjm", "scroll==>" + scrollState);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    adapter.mIsGridViewIdle = true;
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }
}
