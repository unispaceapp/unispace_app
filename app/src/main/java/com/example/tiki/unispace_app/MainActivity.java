package com.example.tiki.unispace_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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


public class MainActivity extends AppCompatActivity{


    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         mainGrid = (GridLayout)findViewById(R.id.mainGrid);
         setOnClickListeners();
    }


    public void setOnClickListeners() {

        CardView item = (CardView)findViewById(R.id.all_classrooms_item);

        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                enableStrictMode();
                openActivity(view, "ALL");
            }
        });

         item = (CardView)findViewById(R.id.search_by_building);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        item = (CardView)findViewById(R.id.nearest);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                openActivity(view, "NEAREST");
            }
        });

        item = (CardView)findViewById(R.id.view_current);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "NO CURRENT CLASSROOM TO DISPLAY", Toast.LENGTH_LONG).show();
            }
        });


        /*TextView item2 = (TextView)findViewById(R.id.textView5);
        item2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                enableStrictMode();
                openActivity(view, "ALL");
            }
        });

        item2 = (TextView)findViewById(R.id.textView3);
        item2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("My Class",MODE_PRIVATE);
                if (sharedPreferences.getAll().isEmpty()){
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View container = (View) layoutInflater.inflate(R.layout.class_warning,null);
                    final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if (Build.VERSION.SDK_INT>=21){
                        popupWindow.setElevation(5.0f);
                    }

                    ImageButton closeButton = (ImageButton) container.findViewById(R.id.ib_close);
                    setContentView(R.layout.class_warning);
                    TextView textView = (TextView) findViewById(R.id.warn_txt);
                    textView.setText("You have no classroom");
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            popupWindow.dismiss();
                            *//*Intent intent = new Intent(getIntent());
                            finish();
                            startActivity(intent);*//*
                        }
                    });
                    setContentView(R.layout.activity_main);
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container);
                    popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
                }else {
                    Intent intent = new Intent(getApplicationContext(), MyClass.class);
                    startActivity(intent);
                }

            }
        });*/

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
