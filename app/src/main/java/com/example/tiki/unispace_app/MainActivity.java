package com.example.tiki.unispace_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.iid.FirebaseInstanceId;

import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private StorageReference s;
    private FirebaseFunctions mFunctions;
    private FirebaseHandler firebaseHandler;
    private RecyclerView mRecyclerView;
    private ClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClassroomObject> res;


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
