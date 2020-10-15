package com.example.capstoneprojectk10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    // Set Timer selama 1.2 Second
    private static int SPLASH_TIME_OUT = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            /*
             * menampilkan splash screen dengan timer
             */

            @Override
            public void run() {
                // method ini akan di eksekusi setelah timer selesai
                // start Main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                // tutup activity ini
                finish();
            }

        }, SPLASH_TIME_OUT);
    }
}