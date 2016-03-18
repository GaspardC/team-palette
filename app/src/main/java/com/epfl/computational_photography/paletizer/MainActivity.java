package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends SlideMenuActivity {

    private static final int RESULT_LOAD_IMG = 0;
    int TAKE_PHOTO_CODE = 2;
    public static int count = 0;
    private String imgDecodableString;
    private String dir;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
    }

    public void choseFromLibrary(View view) {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void takeAPhoto(View view) {


        // Here, the counter will be incremented each time, and the
        // picture taken by camera will be stored as 1.jpg,2.jpg
        // and likewise.
        count++;
        String file = dir+count+".jpg";
        File newfile = new File(file);
        fileName = newfile.getAbsolutePath();

        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.imageMainActivity);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            }
            else if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
                Log.d("CameraDemo", "Pic saved");

                Bitmap bm = resizeIfNeeded(400);
                if (bm == null) {
                    Toast.makeText(getApplicationContext(), "problem with the image", Toast.LENGTH_SHORT).show();
                } else {
                    ImageView preview = (ImageView) findViewById(R.id.imageMainActivity);
                    preview.setImageBitmap(bm);
                    Log.d("CameraDemo", "Pic displayed");

                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private Bitmap resizeIfNeeded(int size) {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
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


    public void goToTransfertActivity(View view) {
        Intent newActivity = new Intent(this, TransfertActivity.class);
        startActivity(newActivity);    }


    public void goToPaletteActivity(View view) {
        Intent newActivity = new Intent(this, PaletteActivity.class);
        startActivity(newActivity);    }

    public void goToStyleActivity(View view) {
        Intent newActivity = new Intent(this, StyleActivity.class);
        startActivity(newActivity);    }
}
