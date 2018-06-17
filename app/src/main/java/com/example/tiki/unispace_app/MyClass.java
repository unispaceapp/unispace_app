package com.example.tiki.unispace_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

/**
 * Class that contains the User's classroom they are
 * currently occupying
 */
public class MyClass extends Activity {

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

        CardView item = (CardView)findViewById(R.id.release_btn);

        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                freeClass();
                cancelNotification();
                finish();
            }
        });

        item = (CardView)findViewById(R.id.to_main);

        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    /**
     * Releases the User's current classroom
     */
    private void freeClass() {
        Calendar calendar = GregorianCalendar.getInstance();
        String hour =  ViewFreeSpaces.hourStrings.get(calendar.get(Calendar.HOUR_OF_DAY));
        if (hour==null){
            hour="all";
        }
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://todaysclassrooms.firebaseio.com");
        /* Gets a reference to the correct hour that needs to be changed */
        final DatabaseReference buildingRef = db.getReference().child(Integer.toString(building));
        /* Gets a reference to the 'hours' field in the occupied classroom */
        DatabaseReference hoursRef = buildingRef.child(Integer.toString(classroom))
                .child("hours");
        if (freeuntil.equals("All Day")){
            freeuntil="all";
        }
        int freeUntilInt = ViewFreeSpaces.hourInts.get(freeuntil);
        int currentHour = 100;
        if (!hour.equals("all")) {
            currentHour = ViewFreeSpaces.hourInts.get(hour);
        }
        int updateUntil = 20;
        if (currentHour<freeUntilInt) {
            updateUntil = freeUntilInt;
        }
        for (int i = currentHour; i < updateUntil; i++) {
            hour = ViewFreeSpaces.hourStrings.get(i);
            hoursRef.child(hour).setValue(0);
        }
    }

    /**
     * Cancels notification that is sent if the classroom
     * is 10 minutes away from it's next lesson
     */
    private void cancelNotification(){
        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        if (nm!=null) {
            nm.cancelAll();
        }
    }
}
