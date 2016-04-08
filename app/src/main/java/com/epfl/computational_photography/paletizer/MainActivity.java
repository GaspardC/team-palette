package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends SlideMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /**
     * @param view when user click on the button Library
     */
    public void choseFromLibrary(View view) {
        PhotoManager.choseFromLibrary(this);
    }

    public void takeAPhoto(View view) {
        PhotoManager.takePhoto(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked from Library
            if (requestCode == PaletizerApplication.RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Bitmap libraryBitmap = PhotoManager.getBitmapFromLibrary(this, requestCode, resultCode, data);
                if (libraryBitmap != null) {
                    ImageView imgView = (ImageView) findViewById(R.id.imageMainActivity);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(libraryBitmap);
                }
            }

            // When an Image is taken by Camera
            if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

                Bitmap cameraBitmap = PhotoManager.getBitmapFromCamera(this, requestCode, resultCode);
                if (cameraBitmap != null) {
                    ImageView preview = (ImageView) findViewById(R.id.imageMainActivity);
                    preview.setImageBitmap(cameraBitmap);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

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
