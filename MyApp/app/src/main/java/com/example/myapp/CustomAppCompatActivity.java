package com.example.myapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

//Force portrait screen orientation in mobile phones
//Force landscape screen orientation in tablets
public class CustomAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (DeviceInfo.TYPE == DeviceType.NONE) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float yInches= metrics.heightPixels/metrics.ydpi;
            float xInches= metrics.widthPixels/metrics.xdpi;
            double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
            if (diagonalInches<6.5){
                DeviceInfo.TYPE = DeviceType.MOBILE;
            } else {
                DeviceInfo.TYPE = DeviceType.TABLET;
            }
        }

        if (DeviceInfo.TYPE == DeviceType.MOBILE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
