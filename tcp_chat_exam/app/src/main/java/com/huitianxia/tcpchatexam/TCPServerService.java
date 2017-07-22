package com.huitianxia.tcpchatexam;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by zhujiaming on 2017/7/18.
 */

public class TCPServerService extends Service {
    private String[] mDefinedMessages = new String[]{
            "你好啊 sb",
            "我屮艸芔茻",
            "老铁们，双击 666",
            "给你讲个笑话，笑话",
    };

    private boolean mIsServiceDestroyed = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TcpServer()).start();
//        App.logi("onCreate,线程创建完毕");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroyed = true;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                App.loge("establish tcp server failed on port 8680.         " + this.getClass().getSimpleName());
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestroyed) {
                try {
//                    App.logi("wait client connect...");
                    final Socket client = serverSocket.accept();
                    if(mIsServiceDestroyed) break;
                    App.logi("accept a client:" + client.getInetAddress());
                    new Thread() {
                        @Override
                        public void run() {
                            //回复客户端
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                                App.logi("reply client  error," + e.getMessage());
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    App.loge("ServerSocket has exception," + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室！");
        while (!mIsServiceDestroyed) {
            String str = in.readLine();
            if (mIsServiceDestroyed)break;
            if (str == null) {
                //客户端断开连接
                App.logi("a client dissconnect...");
                break;
            }
            App.logi("get a msg from client:" + str);
            int i = new Random().nextInt(mDefinedMessages.length);
            String reply = mDefinedMessages[i];
            out.println(reply);
            App.logi("send msg to client:" + reply);
        }
        App.logi("a client quit.");
        Utils.close(in, out, client);
    }

}
