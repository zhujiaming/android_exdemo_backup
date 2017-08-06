package com.jm.imageloadercustom.imgloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.util.Log;

/**
 * Created by zhujiaming on 17/8/6.
 *
 * 图片压缩工具
 */

public class ImageResizer {

    public static final String TAG = "ImageResizer";

    public ImageResizer() {
    }

    /*图片压缩采样*/
    public Bitmap decodeSampledBitmapFromResource(Resources res, @IdRes int id, int reqWidth, int reqHeight) {
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, opts);
        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;

        int inSampleSize = calculateInSampleSize(outWidth, outHeight, reqWidth, reqHeight);

        opts.inJustDecodeBounds = false;
        opts.inSampleSize = inSampleSize;

        return BitmapFactory.decodeResource(res, id, opts);
    }

    private int calculateInSampleSize(int outWidth, int outHeight, int reqWidth, int reqHeight) {

        if (outHeight == 0 || outWidth == 0) {
            return 1;
        }

        int inSampleSize = 1;
        if (outWidth > reqWidth || outHeight > reqHeight) {
            int w = outWidth / 2;
            int h = outHeight / 2;

            while (w / inSampleSize < reqWidth && h / inSampleSize < reqHeight) {
                inSampleSize *= 2;
            }
        }
        Log.d(TAG, "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

}
