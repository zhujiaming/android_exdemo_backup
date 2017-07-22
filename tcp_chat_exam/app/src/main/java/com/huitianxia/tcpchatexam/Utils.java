package com.huitianxia.tcpchatexam;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhujiaming on 2017/7/18.
 */

public class Utils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable:closeables){
            close(closeable);
        }
    }

    public static String fromatCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return format.format(new Date().getTime());
    }
}
