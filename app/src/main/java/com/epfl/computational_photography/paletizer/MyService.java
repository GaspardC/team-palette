package com.epfl.computational_photography.paletizer;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.palette_database.Demo;

/**
 * Created by Gasp on 03/05/16.
 */
public class MyService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Demo demo = new Demo();
        demo.run(getApplicationContext());
    }

}