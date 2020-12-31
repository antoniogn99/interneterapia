package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends CustomAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DeviceInfo.TYPE == DeviceType.MOBILE) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_tablet);
        }
    }

    @Override
    public void onBackPressed() {

    }

    //signIn button behaviour
    public void signInButton(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
    }

    //signUp button behaviour
    public void signUpButton(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
    }

}
