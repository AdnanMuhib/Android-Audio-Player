package com.example.hinakhalid.servicesexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    public void start(View v)

    {

            startService(new Intent(this, playerService.class));

    }
    public void stop(View v)
    {
        stopService(new Intent(this, playerService.class));
    }
}
