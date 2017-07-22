package com.huitianxia.bindpoolexam;

import android.os.RemoteException;

import com.huitianxia.bindpoolexam.aidl.ISecurityCenter;

/**
 * Created by zhujiaming on 2017/7/19.
 */

public class SecurityCenterImpl  extends ISecurityCenter.Stub {
    private static final char SECRET_CODE='^';
    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for(int i = 0;i<chars.length;i++) {
            chars[i] ^=SECRET_CODE;
        }
        return  new String(chars);
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
