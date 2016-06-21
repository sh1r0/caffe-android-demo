caffe-android-demo
==================
An android caffe demo app exploiting caffe pre-trained ImageNet model for image classification

## Quick Start

### Basic

If you want to have a try on this app, please follow the steps below to get the required stuff:
```shell
# 1. get caffe if you don't have one
git clone https://github.com/BVLC/caffe.git
# 2. download model (bvlc_reference_caffenet is used)
cd caffe
./scripts/download_model_binary.py models/bvlc_reference_caffenet/
# 3. push things to your device
adb shell mkdir -p /sdcard/caffe_mobile/
adb push models/bvlc_reference_caffenet/ /sdcard/caffe_mobile/bvlc_reference_caffenet/
```
If the app crashes, first make sure it is not an out of memory issue - modify deploy.prototxt such that the mini batch size is 1 instead of 10
