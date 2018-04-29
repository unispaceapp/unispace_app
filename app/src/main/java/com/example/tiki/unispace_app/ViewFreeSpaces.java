package com.example.tiki.unispace_app;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewFreeSpaces extends AppCompatActivity{

    private FirebaseHandler firebaseHandler;
    private RecyclerView mRecyclerView;
    private ClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClassroomObject> classroomObjects;
    DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    private Map<Integer, List<String>> hoursMap = new HashMap<>();
    private Map<Integer, String> hourStrings = new HashMap<>();
    private int currentPosition;

    private LocationRequest mLocationRequest;
    private boolean mRequestinglocationUpdates = false;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_free_spaces);
        InitializeMembers();
        firebaseHandler = new FirebaseHandler();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        classroomObjects = new ArrayList<>();
        Intent intent = getIntent();
        String request = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (request.equals("ALL")) {
            classroomObjects = firebaseHandler.GetAllClassrooms();
        } else if (request.startsWith("BUILDING")) {
            classroomObjects = firebaseHandler.GetClassroomsByBuilding(Integer.parseInt(request));
        } else {
            Location l = new Gson().fromJson(request, Location.class);
            classroomObjects = firebaseHandler.GetNearestClassrooms(l);
        }

        for (int i = 0; i < classroomObjects.size(); i++) {
            classroomObjects.get(i).setIcon(R.drawable.ic_home_black_24dp);
        }

        mAdapter = new ClassAdapter(classroomObjects);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {

            @Override
            public void onOccupyClick(int position) {
                OccupyClassroom(position);
            }
        });
    }


    private void OccupyClassroom(int position) {
        currentPosition = position;
        Calendar calendar = GregorianCalendar.getInstance();
        String hour = hourStrings.get(calendar.get(Calendar.HOUR));
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://todaysclassrooms.firebaseio.com");
        /* Retrieves which classroom is being occupied */
        final ClassroomObject occupiedClassroom = classroomObjects.get(position);
        /* Gets a reference to the correct hour that needs to be changed */
        final DatabaseReference buildingRef = db.getReference().child(Integer.toString(occupiedClassroom.getBuilding()));
        /* Gets a reference to the 'hours' field in the occupied classroom */
        DatabaseReference hoursRef = buildingRef.child(Integer.toString(occupiedClassroom.getClassroom()))
                .child("hours");
        /* Adds a listener for when a 'hours' child is updated in Firebase DB */
        //TODO: TASK 6 in onChildChange, change hour from current hour till 'free until' to '1' -- When classroom released, will convert back to '0' - Use value of current hour, if now its zero, means must convert hours back to zeros - otherwise, was just updated to '1'
//        ValueEventListener hoursListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int time = dataSnapshot.getValue(Integer.class);
//
//                classroomObjects.get(currentPosition).TEST_CHANGE(Integer.toString(time));
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        };
//        hoursRef.addEvenListener(hoursListener);
        /* Now in Firebase the value is 1 and therefore
        * next user will not have this classroom included
        * in its list of free classrooms */
        hoursRef.child(hour).setValue(1);
        classroomObjects.get(currentPosition).TEST_CHANGE("OCCUPIED");
        mAdapter.notifyItemChanged(position);
        // TODO: TASK 8 (Open the new layout for classroom using occupied classroom info)

    }
    private void InitializeMembers() {


        hourStrings.put(1, "one");
        hourStrings.put(2, "two");
        hourStrings.put(3, "three");
        hourStrings.put(4, "four");
        hourStrings.put(5, "five");
        hourStrings.put(6, "six");
        //TODO: Finish adding...


        ArrayList l = new ArrayList();
        l.add("seven");
        l.add("0");
        hoursMap.put(7, l);
        l = new ArrayList();
        l.add("eight");
        l.add("1");
        hoursMap.put(8, l);
        l = new ArrayList();
        l.add("nine");
        l.add("2");
        hoursMap.put(9, l);
        l = new ArrayList();
        l.add("ten");
        l.add("3");
        hoursMap.put(10, l);
        l = new ArrayList();
        l.add("eleven");
        l.add("4");
        hoursMap.put(11, l);
        l = new ArrayList();
        l.add("twelve");
        l.add("5");
        hoursMap.put(12, l);
        l = new ArrayList();
        l.add("one");
        l.add("6");
        hoursMap.put(13, l);
        l = new ArrayList();
        l.add("two");
        l.add("7");
        hoursMap.put(14, l);


    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }



}



                /*DatabaseReference room = building.child(Integer.toString(occupiedClassroom.getClassroom()));
                DatabaseReference hours = room.child("hours");
                int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3;
                DatabaseReference hour = hours.child(hoursMap.get(time).get(1)).child(hoursMap.get(time).get(0));
                hour.setValue("1");*/

