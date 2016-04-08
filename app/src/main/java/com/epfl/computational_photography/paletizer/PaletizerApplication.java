package com.epfl.computational_photography.paletizer;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Gasp on 23/03/16.
 */
public class PaletizerApplication extends Application {

    public static final int RESULT_LOAD_IMG = 0;
    public static final int TAKE_PHOTO_CODE = 2;
    public static String dir;
    public static String fileName;


    @Override
    public void onCreate() {
        super.onCreate();
        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/paletizer/";
        File newdir = new File(dir);
        newdir.mkdirs();
    }
}
