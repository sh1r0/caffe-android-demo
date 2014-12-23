package com.sh1r0.caffe_android_demo;

/**
 * Created by shiro on 2014/10/9.
 */
public class ImageNet {
    public native void enableLog(boolean enabled);
    public native int initTest(String modelPath, String weightsPath);
    public native int runTest(String imgPath);
}
