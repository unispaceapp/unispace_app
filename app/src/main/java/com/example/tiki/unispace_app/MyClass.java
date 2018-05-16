package com.example.tiki.unispace_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import com.example.tiki.unispace_app.ViewFreeSpaces;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyClass extends AppCompatActivity {

    ArrayList<ClassroomObject> classroomObjects;
    private ClassroomObject classroomObj;
    private Integer building;
    private Integer classroom;
    private String freeuntil;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        classroomObjects = new ArrayList<>();

        sharedPreferences = getSharedPreferences("My Class",MODE_PRIVATE);
        building = sharedPreferences.getInt("Building",-1);
        classroom = sharedPreferences.getInt("Class",-1);
        freeuntil = sharedPreferences.getString("Freeuntil","Not Exist");

        TextView buildingView = (TextView) findViewById(R.id.building_place);
        TextView classView = (TextView) findViewById(R.id.class_place);
        TextView freeView = (TextView) findViewById(R.id.freeuntil_place);

        buildingView.setText(Integer.toString(building));
        classView.setText(Integer.toString(classroom));
        freeView.setText(freeuntil);

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        Button releaseBtn = (Button) findViewById(R.id.release_btn);
        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                freeClass();
                startActivity(intent);
            }
        });

        Button homeBtn = (Button) findViewById(R.id.to_main);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });



    }

    private void freeClass() {
        Calendar calendar = GregorianCalendar.getInstance();
        String hour =  ViewFreeSpaces.hourStrings.get(calendar.get(Calendar.HOUR_OF_DAY));
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://todaysclassrooms.firebaseio.com");
        /* Gets a reference to the correct hour that needs to be changed */
        final DatabaseReference buildingRef = db.getReference().child(Integer.toString(building));
        /* Gets a reference to the 'hours' field in the occupied classroom */
        DatabaseReference hoursRef = buildingRef.child(Integer.toString(classroom))
                .child("hours");
        hoursRef.child(hour).setValue(0);
    }
}