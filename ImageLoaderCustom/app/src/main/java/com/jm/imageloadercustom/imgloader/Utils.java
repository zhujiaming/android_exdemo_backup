package com.jm.imageloadercustom.imgloader;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by zhujiaming on 17/8/6.
 */

public class Utils {

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int[] getScreenWidthAndHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        int[] sw = new int[]{outSize.x, outSize.y};
        return sw;
    }
}
