package com.huitianxia.bindpoolexam.aidl;
interface ISecurityCenter{
    String encrypt(String content);
    String decrypt(String password);
}