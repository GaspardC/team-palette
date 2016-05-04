package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.epfl.computational_photography.paletizer.palette_database.Demo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView title = (TextView) findViewById(R.id.title_palette);
        TextView subtitle = (TextView) findViewById(R.id.subtitle_palette);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "Pacifico.ttf");
        title.setTypeface(face);
        subtitle.setTypeface(face);
        // Starts the IntentService
        startService(new Intent(this, MyService.class));




        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);
    }


}
