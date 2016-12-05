package com.sh1r0.caffe_android_lib;

import java.nio.charset.StandardCharsets;

public class CaffeMobile {
    private static byte[] stringToBytes(String s) {
        return s.getBytes(StandardCharsets.US_ASCII);
    }

    public native void setNumThreads(int numThreads);

    public native void enableLog(boolean enabled);  // currently nonfunctional

    public native int loadModel(String modelPath, String weightsPath);  // required

    private native void setMeanWithMeanFile(String meanFile);

    private native void setMeanWithMeanValues(float[] meanValues);

    public native void setScale(float scale);

    public native float[] getConfidenceScore(byte[] data, int width, int height);

    public float[] getConfidenceScore(String imgPath) {
        return getConfidenceScore(stringToBytes(imgPath), 0, 0);
    }

    public native int[] predictImage(byte[] data, int width, int height, int k);

    public int[] predictImage(String imgPath, int k) {
        return predictImage(stringToBytes(imgPath), 0, 0, k);
    }

    public int[] predictImage(String imgPath) {
        return predictImage(imgPath, 1);
    }

    public native float[][] extractFeatures(byte[] data, int width, int height, String blobNames);

    public float[][] extractFeatures(String imgPath, String blobNames) {
        return extractFeatures(stringToBytes(imgPath), 0, 0, blobNames);
    }

    public void setMean(float[] meanValues) {
        setMeanWithMeanValues(meanValues);
    }

    public void setMean(String meanFile) {
        setMeanWithMeanFile(meanFile);
    }
}
