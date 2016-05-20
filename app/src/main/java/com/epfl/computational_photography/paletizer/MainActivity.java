package com.epfl.computational_photography.paletizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.fastTranfer.TransferActivity;

public class MainActivity extends SlideMenuActivity {



    String[] perms =  { "android.permission.CAMERA",
            "android.permission.SYSTEM_ALERT_WINDOW",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.INTERNET",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.ACCESS_NETWORK_STATE" };

    int permsRequestCode = 200;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("rrrrr", perms.toString());
         sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(canMakeSmores()){
           boolean edited = sharedpreferences.getBoolean("edited",false);
            if(!edited){
                requestPermissions(perms, permsRequestCode);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("edited",true);
                editor.commit();
            }


        }

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


    private boolean hasPermission(String permission){

        if(canMakeSmores()){

            return(checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }
    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    

//    private boolean shouldWeAsk(String permission){
//        sharedPreferences = getSharedPreferences("permission",1);
//        return (sharedPreferences.getBoolean(permission, true));
//
//    }
//
//
//
//    private void markAsAsked(String permission){
//
//        sharedPreferences.edit().putBoolean(permission, false).apply;
//
//    }
}
