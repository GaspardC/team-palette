package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette_database.Demo;
import com.epfl.computational_photography.paletizer.palette.extractor.Extractor;

public class PaletteActivity extends SlideMenuActivity {

    private ImageView sourceView;
    private Bitmap sourceImage;
    static {
        System.loadLibrary("opencv_java3"); //load opencv_java lib
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        // Palette database demo
        sourceView = (ImageView) findViewById(R.id.sourceImage);
    }

    public void chooseImage(View view) {
        PhotoManager.choseFromLibrary(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked from Library
            if (requestCode == PaletizerApplication.RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Bitmap libraryBitmap = PhotoManager.getBitmapFromLibrary(this, requestCode, resultCode, data);
                if (libraryBitmap != null) {
                    sourceImage = Bitmap.createScaledBitmap(libraryBitmap, 250, 250, false);
                    sourceView.setImageBitmap(sourceImage);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void extractPalette(View view) {
        LinearLayout paletteContainer = (LinearLayout) findViewById(R.id.palette_container);
        int[] rgbCenters = new Extractor().extract(sourceImage);
        for(int i = 0; i < paletteContainer.getChildCount(); i++) {
            paletteContainer.getChildAt(i).setBackgroundColor(rgbCenters[i]);
        }
    }

}
