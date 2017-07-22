package com.huitianxia.bindpoolexam;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.huitianxia.bindpoolexam.aidl.ICompute;
import com.huitianxia.bindpoolexam.aidl.ISecurityCenter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void conn(View view) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        BinderPool binderPool = BinderPool.getBinderPool(MainActivity.this);
                        IBinder sercurityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
                        ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(sercurityBinder);

                        String word = "你好 安卓";
                        Log.i("zjm", "word:" + word);
                        try {
                            String encrypt = securityCenter.encrypt(word);
                            Log.i("zjm", "word(encrypt):" + encrypt);
                            String decrypt = securityCenter.decrypt(encrypt);
                            Log.i("zjm", "word(decrypt):" + decrypt);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                        IBinder iBinder = BinderPool.getBinderPool(MainActivity.this).queryBinder(BinderPool.BINDER_COMPUTE);
                        ICompute iCompute = ComputeImpl.asInterface(iBinder);
                        Log.i("zjm", "\n 计算1+1");
                        try {
                            int add = iCompute.add(1, 2);
                            Log.i("zjm", "结果:" + add);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).start();
    }
}
