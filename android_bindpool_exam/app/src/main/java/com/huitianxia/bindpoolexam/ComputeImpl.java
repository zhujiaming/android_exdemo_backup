package com.huitianxia.bindpoolexam;

import android.os.RemoteException;

import com.huitianxia.bindpoolexam.aidl.ICompute;

/**
 * Created by zhujiaming on 2017/7/19.
 */

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
