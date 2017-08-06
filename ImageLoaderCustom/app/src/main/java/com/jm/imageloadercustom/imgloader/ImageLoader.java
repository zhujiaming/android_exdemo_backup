package com.jm.imageloadercustom.imgloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class ImageLoader {
    private final static String TAG = "ImageLoader";
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;


    private boolean mIsDiskLruCacheCreated;
    private final Context mContext;
    private final LruCache<String, Bitmap> mLruCache;
    private DiskLruCache mDiskLruCache;


    public ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;//取最大内存的1/8
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();//api19 android4.4w+
                int byteCount2 = value.getRowBytes() * value.getHeight() / 1024;
                return byteCount2;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, TAG);
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        if (getUseableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                /*第三个参数-->单个节点所对应的数据个数，一般设置为1即可0.0*/
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "DiskLruCache 初始化失败");
            }
        }
    }

    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    private File getDiskCacheDir(Context mContext, String tag) {
        File catchDir = new File(Environment.getExternalStorageDirectory(), "imageCache");
        return catchDir;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUseableSpace(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return file.getUsableSpace();
        }

        final StatFs stats = new StatFs(file.getPath());
        return stats.getBlockSizeLong() * stats.getAvailableBlocksLong();
    }
}
