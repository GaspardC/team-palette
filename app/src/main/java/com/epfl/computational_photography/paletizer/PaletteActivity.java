package com.epfl.computational_photography.paletizer;

import android.os.Bundle;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette_database.Demo;

public class PaletteActivity extends SlideMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        // Palette database demo
        Demo.run(getApplicationContext());
    }
}
