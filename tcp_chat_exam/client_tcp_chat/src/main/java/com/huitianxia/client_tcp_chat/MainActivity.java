package com.huitianxia.client_tcp_chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final int MESSAGE_SOCKET_CONNECTED = 0;
    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;

    private Socket mClientSocket;
    private PrintWriter mPrintWriter;

    private boolean isFinished = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SOCKET_CONNECTED:
                    tvMsg.append("服务器连接成功！" + "\n");
                    break;
                case MESSAGE_RECEIVE_NEW_MSG:
                    tvMsg.append((String) msg.obj + "\n");
                    break;
            }
        }
    };
    private TextView tvMsg;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        editText = (EditText) findViewById(R.id.et_input);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new TcpClient()).start();
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String input = editText.getText().toString();
                        if (mPrintWriter != null && !TextUtils.isEmpty(input)) {
                            String showMsg = "我(" + Utils.fromatCurrentDate() + ")：" + input;
                            App.logi("send:"+showMsg);
                            mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMsg).sendToTarget();
                            mPrintWriter.println(input);
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isFinished = true;
        try {
            if (mClientSocket != null)
                mClientSocket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.close(mClientSocket, mPrintWriter);
    }

    private class TcpClient implements Runnable {

        @Override
        public void run() {
            Socket socket = null;
            mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, "正在连接服务端...").sendToTarget();
            while (socket == null && !isFinished) {
                try {
                    socket = new Socket("localhost", 8688);
                    mClientSocket = socket;
                    mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                    App.logi("connect server success");
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, "连接服务端成功！").sendToTarget();
                } catch (IOException e) {
                    SystemClock.sleep(1000);
                    App.logi("connect server failed,retrying...");
                }
            }
            //连接服务器成功，与服务器交谈
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!MainActivity.this.isFinished) {
                    String fromServer = bufferedReader.readLine();
                    if (MainActivity.this.isFinished) break;
                    if (fromServer != null) {
                        String showMsg = "server(" + Utils.fromatCurrentDate() + ")：" + fromServer;
                        App.logi(showMsg);
                        mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showMsg).sendToTarget();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                App.loge("contact with server err" + e.getMessage());
            }
        }
    }


}
