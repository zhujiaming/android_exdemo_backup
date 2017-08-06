package com.jm.imageloadercustom.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jm.imageloadercustom.R;
import com.jm.imageloadercustom.imgloader.ImageLoader;
import com.jm.imageloadercustom.imgloader.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class ImageAdapter extends BaseAdapter {

    private List<String> datas = new ArrayList<>();
    private Context mContext;
    public boolean mIsGridViewIdle = true;
    public boolean mCanGetBitmapFromNetWork = true;
    private ImageLoader mImageLoader;
    private int mImageWidth;

    public ImageAdapter(List<String> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
        this.mImageLoader = ImageLoader.build(mContext);
        mImageWidth = Utils.getScreenWidthAndHeight(mContext)[0] / 3;
        Log.e("zjm", "width:" + mImageWidth);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ImageView iv = vh.imageView;
        final String tag = (String) iv.getTag();
        final String uri = getItem(position);
        if (!uri.equals(tag)) {
            iv.setImageResource(R.mipmap.ic_launcher);
        }
//        Log.i("zjm","加载图片。。。。");

        if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
            Log.i("zjm","加载图片。。。。");
            iv.setTag(uri);
            mImageLoader.bindBitmap(getItem(position), iv, mImageWidth, mImageWidth);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
