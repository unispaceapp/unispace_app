package com.example.tiki.unispace_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Main activity, first activity to be displayed when application
 * loads
 */
public class MainActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mainGrid = (GridLayout)findViewById(R.id.mainGrid);
         sharedPreferences = getSharedPreferences("My Class",MODE_PRIVATE);
         setOnClickListeners();
    }


    /**
     * Sets listeners for each button on home screen
     */
    public void setOnClickListeners() {

        CardView item = (CardView)findViewById(R.id.all_classrooms_item);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (sharedPreferences.getAll().isEmpty()) {
                    enableStrictMode();
                    openActivity(view, "ALL");
                } else {
                    // user cannot occupy more than one classroom at a time
                    Toast.makeText(MainActivity.this, "User Can't take more than one class", Toast.LENGTH_LONG).show();
                }
            }
        });

         item = (CardView)findViewById(R.id.search_by_building);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (sharedPreferences.getAll().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User Can't take more than one class", Toast.LENGTH_LONG).show();
                }

            }
        });

        item = (CardView)findViewById(R.id.nearest);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (sharedPreferences.getAll().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User Can't take more than one class", Toast.LENGTH_LONG).show();
                }
            }
        });

        item = (CardView)findViewById(R.id.view_current);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getAll().isEmpty()) {
                    Toast.makeText(MainActivity.this, "NO CURRENT CLASSROOM TO DISPLAY", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MyClass.class);
                    startActivity(intent);
                }
            }
        });

    }


    /**
     * Opens a new activiy
     * @param view which view to use
     * @param request user request if neccessary
     */
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

    /**
     * Returns to previous activity when user presses back on phone
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
