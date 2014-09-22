package com.sh1r0.caffe_android_demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class MainActivity extends Activity implements CNNListener {
    private static final String LOG_TAG = "MainActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static String[] IMAGENET_CLASSES;

    private Uri fileUri;
    private Button btnCamera;
    private ImageView ivCaptured;
    private TextView tvLabel;

    static {
        System.loadLibrary("caffe");
        System.loadLibrary("mira-cnn");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivCaptured = (ImageView) findViewById(R.id.ivCaptured);
        tvLabel = (TextView) findViewById(R.id.tvLabel);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                btnCamera.setEnabled(false);
                tvLabel.setText("");
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
            }
        });

        AssetManager am = this.getAssets();
        try {
            InputStream is = am.open("synset_words.txt");
            Scanner sc = new Scanner(is);
            List<String> lines = new ArrayList<String>();
            while (sc.hasNextLine()) {
                final String temp = sc.nextLine();
                lines.add(temp.substring(temp.indexOf(" ") + 1));
            }
            IMAGENET_CLASSES = lines.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(fileUri.getPath());
            Log.d(LOG_TAG, fileUri.getPath());
            Log.d(LOG_TAG, String.valueOf(bmp.getHeight()));
            Log.d(LOG_TAG, String.valueOf(bmp.getWidth()));
            ivCaptured.setImageBitmap(bmp);
            CNNTask cnnTask = new CNNTask(MainActivity.this);
            cnnTask.execute(fileUri.getPath());
        } else {
            btnCamera.setEnabled(true);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private native int initTest();
    private native int runTest(String imgPath);

    private class CNNTask extends AsyncTask<String, Void, Integer> {
        private CNNListener listener;

        public CNNTask(CNNListener listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            initTest();
            return runTest(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            listener.onTaskCompleted(integer);
            super.onPostExecute(integer);
        }
    }

    @Override
    public void onTaskCompleted(int result) {
        tvLabel.setText(IMAGENET_CLASSES[result]);
        Log.d(LOG_TAG, "done !!");
        btnCamera.setEnabled(true);
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()) {
            if (! mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
