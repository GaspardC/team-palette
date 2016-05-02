package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette.extractor.Transferer;


public class TransfertActivity extends SlideMenuActivity {

    Bitmap bitSource;
    Bitmap bitTarget;
    private Bitmap bitmapResult;

    private ImageView focusImage;
    boolean isSource;
    static {
        System.loadLibrary("opencv_java3"); //load opencv_java lib
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);
        focusImage = (ImageView) findViewById(R.id.transfertSourceImage);
        bitSource = ((BitmapDrawable)focusImage.getDrawable()).getBitmap();
        focusImage = (ImageView) findViewById(R.id.transfertTargetImage);
        bitTarget = ((BitmapDrawable)focusImage.getDrawable()).getBitmap();


    }

    /**
     * @param view when user click on the button Library
     */
    public void transfertChoseFromLibrary(View view) {
        if ("source".equals(view.getTag().toString())){
            focusImage = (ImageView) findViewById(R.id.transfertSourceImage);
            isSource = true;
        }
        else{
            focusImage = (ImageView) findViewById(R.id.transfertTargetImage);
            isSource = false;
        }

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
                    // Set the Image in ImageView after decoding the String
                    focusImage.setImageBitmap(libraryBitmap);
                    if(isSource){
                        bitSource = libraryBitmap;
                    }
                    else{
                        bitTarget = libraryBitmap;
                    }
                }
            }

            // When an Image is taken by Camera
            if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

                Bitmap cameraBitmap = PhotoManager.getBitmapFromCamera(this, requestCode, resultCode);
                if (cameraBitmap != null) {
                    focusImage.setImageBitmap(cameraBitmap);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void computeTransfert(View view) {
        if(bitSource == null || bitTarget == null){
            Toast.makeText(this, "Chose 2 photos before", Toast.LENGTH_LONG)
                    .show();
        } else {
            ImageView imRes = (ImageView) (findViewById(R.id.transfertImageResult));
            bitmapResult = new Transferer().transfer(bitSource, bitTarget);
            imRes.setImageBitmap(bitmapResult);
        }
    }

}
