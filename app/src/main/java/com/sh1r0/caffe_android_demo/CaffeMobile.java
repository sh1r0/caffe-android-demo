package com.sh1r0.caffe_android_demo;

/**
 * Created by shiro on 3/26/15.
 */
public class CaffeMobile {
    public native void enableLog(boolean enabled);
    public native int loadModel(String modelPath, String weightsPath);
    public native int predictImage(String imgPath);
}
