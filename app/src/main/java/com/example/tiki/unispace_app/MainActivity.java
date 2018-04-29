package com.example.tiki.unispace_app;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1 = (Button)findViewById(R.id.freeSpaceButton);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableStrictMode();
                openActivity(view, "ALL");
            }
        });

        Button buildButton = (Button)findViewById(R.id.byBuilding);
        buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableStrictMode();
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);

            }

        });

        Button nearestButton = (Button)findViewById(R.id.nearestButton);
        nearestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableStrictMode();
                openActivity(view, "NEAREST");
            }

        });


    }


    public void openActivity(View view, String request) {
        Intent intent = new Intent(this, ViewFreeSpaces.class);
        intent.putExtra(Intent.EXTRA_TEXT, request);
        startActivity(intent);
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


}
