package com.example.tiki.unispace_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

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
                setContentView(R.layout.bybuilding);
                //final EditText et = (EditText)findViewById(R.id.building);
                final Button b3 = (Button)findViewById(R.id.search);
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText et = (EditText)findViewById(R.id.building);
                        String wantedBuilding = et.getText().toString();
                        openActivity(view, wantedBuilding);
                    }
                });
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
