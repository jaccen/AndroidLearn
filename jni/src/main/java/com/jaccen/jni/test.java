package com.jaccen.jni;

public class test {
    static {
        System.loadLibrary("my-native-lib");  //通过静态块，加载原生库
    }

    public native String stringFromJNI();  //定义一个native 方法
}
