package com.laoschool.screen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.laoschool.R;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                startLogin();
            }
        }, SPLASH_TIME_OUT);
    }

    private void startLogin() {
        Intent intent = new Intent(SplashScreen.this, ScreenLogin.class);
        startActivity(intent);
        this.finish();
    }
}
