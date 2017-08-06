package com.jm.imageloadercustom.imgloader;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.jm.imageloadercustom.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class ImageLoader {
    private final static String TAG = "ImageLoader";

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 5;//50Mb
    private static final int IO_BUFFER_SIZE = 1024 * 8;//8k
    private static final int DISK_CACHE_INDEX = 0;
    public static final int TAG_KEY_URI = R.id.imageload_key;
    private static final int MESSAGE_POST_RESULT = 1;


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUMPOOLSIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "ImageLoader #" + mCount.getAndIncrement());
        }
    };
    private static final Executor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(CORE_POOL_SIZE,
                    MAXIMUMPOOLSIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    sThreadFactory
            );


    private boolean mIsDiskLruCacheCreated;
    private final Context mContext;
    private final LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    private final ImageResizer mImagerResizer = new ImageResizer();


    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Log.w(TAG, "set image bitmap,but url has changed,ignored");
            }
        }
    };

    public ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;//取最大内存的1/8
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
//                int byteCount = value.getByteCount();//api19 android4.4w+
                int byteCount2 = value.getRowBytes() * value.getHeight() / 1024;
                return byteCount2;
            }
        };
        File diskCacheDir = getDiskCacheDir(mContext, "jmxxxxx");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        long useableSpace = getUseableSpace(diskCacheDir);
        if (useableSpace >= DISK_CACHE_SIZE) {
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


    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    public void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i(TAG, "loadBitmapFromMemCache:" + uri);
            return;
        }

        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult loaderResult = new LoaderResult(imageView, uri, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDiskCache,url:" + uri);
                return bitmap;
            }

            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,url:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error,DisLruCache is not created");
            bitmap = downloadBitmapFromUrl(uri);
        }

        return bitmap;
    }

    /**
     * 将bitmap添加到memCache
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从memCache中读取bitmap
     *
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 从网络下载图片并写入到diskcache，写入diskcache过程会同时写入memCache中，再返回bitmap
     *
     * @param url
     * @param reqWitdh
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWitdh, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI thread!");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor edit = mDiskLruCache.edit(key);
        if (edit != null) {
            OutputStream outputStream = edit.newOutputStream(DISK_CACHE_INDEX);
            if (downloadUrlToStream(url, outputStream)) {
                edit.commit();
            } else {
                edit.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWitdh, reqHeight);
    }

    /**
     * diskcache不可用，直接下载图片，压缩图片，返回bitmap
     *
     * @param urlStr
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlStr) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            Log.e(TAG, "Error in downloadBitmap:" + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            Utils.closeStream(in);
        }
        return bitmap;
    }


    /**
     * 从disklrucache中读取图片，读取到了添加到memoryCache中，再返回bitmap
     *
     * @param url
     * @param reqWitdh
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWitdh, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "load bitmap from UI thread,it's not 建议");
        }

        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap = null;
        String key = hashKeyFromUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImagerResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWitdh, reqHeight);
            snapshot.close();
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }

        return bitmap;
    }

    /**
     * 从memoryCache中读取图片
     *
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        String key = hashKeyFromUrl(url);
        Bitmap bitmap = getBitmapFromMemoryCache(key);
        return bitmap;
    }

    /**
     * 读取网络的inputStream 写入到outputStream
     *
     * @param urlStr
     * @param outputStream
     * @return
     */
    private boolean downloadUrlToStream(String urlStr, OutputStream outputStream) {
        HttpURLConnection conn = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(conn.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "download Bitmap failed," + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            Utils.closeStream(out);
            Utils.closeStream(in);
        }
        return false;
    }

    /**
     * 获取url所对应的hashkey
     *
     * @param url
     * @return
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        int byteLen = digest.length;
        for (int i = 0; i < byteLen; i++) {
            String hex = Integer.toHexString(0xff & digest[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    private File getDiskCacheDir(Context mContext, String uniqueName) {
//        File catchDir = new File(Environment.getExternalStorageDirectory(), "imageCache");
        boolean hasSDcard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (hasSDcard) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUseableSpace(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return file.getUsableSpace();
        }

        final StatFs stats = new StatFs(file.getPath());
        return stats.getBlockSizeLong() * stats.getAvailableBlocksLong();
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
