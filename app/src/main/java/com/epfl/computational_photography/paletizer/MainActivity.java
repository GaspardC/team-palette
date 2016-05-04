package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.fastTranfer.TransferActivity;

public class MainActivity extends SlideMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goToPaletteActivity(View view) {
        Intent newActivity = new Intent(this, PaletteActivity.class);
        startActivity(newActivity);    }

    public void goToTransfertActivity_static(View view) {
        Intent newActivity = new Intent(this, TransferActivity.class);
        newActivity.putExtra("static",true);
        startActivity(newActivity);
    }

    public void goToTransfertActivity_Live(View view) {
        Intent newActivity = new Intent(this, TransferActivity.class);
        newActivity.putExtra("static",false);
        startActivity(newActivity);
    }
}
