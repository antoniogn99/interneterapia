package com.example.myapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity {

    public static String videoPath;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window fullscreen, remove title bar and force landscape orientation
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Set content
        setContentView(R.layout.activity_player);

        //Prepare the video
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();
        videoView.start();
        mediaController = new MediaController(this){

            //Override these methods to go back when back button is pressed

            @Override
            public void show() {
                super.show();
                requestFocus();
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    super.hide(); //Hide mediaController
                    PlayerActivity.this.onBackPressed(); //Go to the previous activity
                    return true; //Finish
                }
                //If not Back button, other button (volume) work as usual.
                return super.dispatchKeyEvent(event);
            }
        };
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
    }
}
