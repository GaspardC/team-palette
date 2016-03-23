package com.epfl.computational_photography.paletizer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gasp on 23/03/16.
 */
public class PhotoManager {

    public static final int RESULT_OK           = -1;
    private static String imgDecodableString;
    public static int count = 0;




    public static void takePhoto(Activity activity){


        // Here, the counter will be incremented each time, and the
        // picture taken by camera will be stored as 1.jpg,2.jpg
        // and likewise.
        SimpleDateFormat sdf = new SimpleDateFormat("dd_HHmm");
        String currentDateandTime = sdf.format(new Date());
        String file = PaletizerApplication.dir+currentDateandTime+".jpg";
        File newfile = new File(file);
        PaletizerApplication.fileName = newfile.getAbsolutePath();

        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PaletizerApplication.TAKE_PHOTO_CODE);
    }

    public static void choseFromLibrary(Activity activity){
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        activity.startActivityForResult(galleryIntent, PaletizerApplication.RESULT_LOAD_IMG);
    }


    public static Bitmap getBitmapFromLibrary(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == PaletizerApplication.RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = activity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


        }
        else{
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
        return BitmapFactory.decodeFile(imgDecodableString);

    }

    public static Bitmap getBitmapFromCamera(Activity activity, int requestCode, int resultCode) {

        if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");

            Bitmap bm = resizeIfNeeded(400);
            if (bm == null) {
                Toast.makeText(activity.getApplicationContext(), "problem with the image", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("CameraDemo", "Pic displayed");
                return bm;

            }
        }
        return null;
    }


    private static Bitmap resizeIfNeeded(int size) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        String fileName = PaletizerApplication.fileName;
        BitmapFactory.decodeFile(fileName, bounds);


        // Find the correct scale value. It should be the power of 2.
        int scale = 1;
        while(bounds.outWidth / scale / 2 >= size &&
                bounds.outHeight / scale / 2 >= size) {
            scale *= 2;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = scale;
        return BitmapFactory.decodeFile(fileName, opts);
    }
}
