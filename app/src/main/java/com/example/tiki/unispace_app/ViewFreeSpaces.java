package com.example.tiki.unispace_app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.transition.Explode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewFreeSpaces extends AppCompatActivity{

    private FirebaseHandler firebaseHandler;
    private RecyclerView mRecyclerView;
    private ClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClassroomObject> classroomObjects;
    DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    private Map<Integer, List<String>> hoursMap = new HashMap<>();
    public static Map<Integer, String> hourStrings = new HashMap<>();
    public static Map<String, Integer> hourInts = new HashMap<>();
    private int currentPosition;
    private ArrayList<Integer> deletePositions = new ArrayList<>();

    private LocationRequest mLocationRequest;
    private boolean mRequestinglocationUpdates = false;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;



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
            try {
                classroomObjects = firebaseHandler.GetAllClassrooms();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //TODO parse request
        } else if (request.contains("building")) {
            try {
                classroomObjects = firebaseHandler.GetClassroomsByBuilding(request);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = new Gson().fromJson(request, Location.class);
            try {
                classroomObjects = firebaseHandler.GetNearestClassrooms(location);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (classroomObjects.size()==0){
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View container = (View) layoutInflater.inflate(R.layout.class_warning,null);
            //popupWindow = new PopupWindow(container,700, 700,true);
            popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT>=21){
                popupWindow.setElevation(5.0f);
            }
            ImageButton closeButton = (ImageButton) container.findViewById(R.id.ib_close);
            /*setContentView(R.layout.class_warning);
            TextView textView = (TextView) findViewById(R.id.warn_txt);
            textView.setText("This class has been taken recently.");*/
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    popupWindow.dismiss();
                   // mAdapter.notifyItemRemoved(currentPosition);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
//                    deletePositions.add(position);
//                    Bundle b = new Bundle();
//                    b.putIntegerArrayList("Positions", deletePositions);
//                    intent.putExtras(b);
//                    finish();
//                    startActivity(intent);
                }
            });
            setContentView(R.layout.activity_view_free_spaces);
            relativeLayout = (RelativeLayout) findViewById(R.id.relative);
            relativeLayout.post(new Runnable() {
                public void run() {
                    popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);
                }
            });
        }

        for (int i = 0; i < classroomObjects.size(); i++) {
//            classroomObjects.get(i).setFreeUntil("five");
            classroomObjects.get(i).setIcon(R.drawable.ic_home_black_24dp);
            if (classroomObjects.get(i).getFreeUntil().equals("0")){
                classroomObjects.get(i).setFreeUntil("All Day");
            }
        }


        Bundle b = getIntent().getExtras();
        if (b.getIntegerArrayList("Positions")!=null){
            deletePositions.addAll(b.getIntegerArrayList("Positions"));
        }
        if (deletePositions.size() >0) {
            for (int i=0; i< deletePositions.size(); i++) {
                classroomObjects.remove(deletePositions.get(i).intValue());
            }
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


    private void OccupyClassroom(final int position) {
        currentPosition = position;
        Calendar calendar = GregorianCalendar.getInstance();
        String hour = hourStrings.get(calendar.get(Calendar.HOUR_OF_DAY));
        if (hour==null) {
            hour="all";
        }
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://todaysclassrooms.firebaseio.com");
        /* Retrieves which classroom is being occupied */
        ClassroomObject occupiedClassroom = classroomObjects.get(position);
        /* Gets a reference to the correct hour that needs to be changed */
        DatabaseReference buildingRef = db.getReference().child(Integer.toString(occupiedClassroom.getBuilding()));
        /* Gets a reference to the 'hours' field in the occupied classroom */
        DatabaseReference hoursRef = buildingRef.child(Integer.toString(occupiedClassroom.getClassroom()))
                .child("hours");

        DatabaseReference hourRef = hoursRef.child(hour);

        final Integer[] value = new Integer[4];
        ValueEventListener hourListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               System.out.println("***********************" + dataSnapshot.getValue(Integer.class));
               value[0] = dataSnapshot.getValue(Integer.class);
               System.out.println("In data changed");
               Toast.makeText(ViewFreeSpaces.this, "Value is " + value[0], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        hourRef.addListenerForSingleValueEvent(hourListener);
        /* Adds a listener for when a 'hours' child is updated in Firebase DB */
        //TODO: TASK 6 in onChildChange, change hour from current hour till 'free until' to '1' --
        // When classroom released, will convert back to '0' - Use value of current hour, if now its zero, means must convert hours back to zeros - otherwise, was just updated to '1'
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
        //hoursRef.child(hour).setValue(9);
        //occupiedClassroom.setFreeUntil("two");
        boolean occupied = false;
        if (!hour.equals("all")) {
            occupied = finalCheckForClass(hourRef, hour, value[0]);
        }
        if (!occupied){
            updateHoursInDB(occupiedClassroom, hoursRef, hour);
            //classroomObjects.get(currentPosition).TEST_CHANGE("OCCUPIED");
            //classroomObjects.remove(currentPosition);
            //mAdapter.notifyItemRemoved(currentPosition);
            mAdapter.notifyItemChanged(position);
            // TODO: TASK 8 (Open the new layout for classroom using occupied classroom info)
            SharedPreferences sharedPreferences = getSharedPreferences("My Class", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Building", occupiedClassroom.getBuilding());
            editor.putInt("Class", occupiedClassroom.getClassroom());
            editor.putString("Freeuntil", occupiedClassroom.getFreeUntil());
            editor.apply();
            if (!occupiedClassroom.getFreeUntil().equals("All Day")) {
                scheduleNotification(getApplicationContext(), hourInts.get(occupiedClassroom.getFreeUntil()) - 1);
            }
            Intent intent = new Intent(getApplicationContext(), MyClass.class);
            startActivity(intent);
        } else {
            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View container = (View) layoutInflater.inflate(R.layout.class_warning,null);
            //popupWindow = new PopupWindow(container,700, 700,true);
            popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (Build.VERSION.SDK_INT>=21){
                popupWindow.setElevation(5.0f);
            }
            ImageButton closeButton = (ImageButton) container.findViewById(R.id.ib_close);
            setContentView(R.layout.class_warning);
            TextView textView = (TextView) findViewById(R.id.warn_txt);
            textView.setText("This class has been taken recently.");
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    popupWindow.dismiss();
                    mAdapter.notifyItemRemoved(currentPosition);
//                    Intent intent = new Intent(getIntent());
//                    deletePositions.add(position);
//                    Bundle b = new Bundle();
//                    b.putIntegerArrayList("Positions", deletePositions);
//                    intent.putExtras(b);
//                    finish();
//                    startActivity(intent);
                }
            });
            setContentView(R.layout.activity_view_free_spaces);
            relativeLayout = (RelativeLayout) findViewById(R.id.relative);
            popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);



        }




    }
    private void InitializeMembers() {


        hourStrings.put(13, "one");
        hourStrings.put(14, "two");
        hourStrings.put(15, "three");
        hourStrings.put(16, "four");
        hourStrings.put(17, "five");
        hourStrings.put(18, "six");
        hourStrings.put(19, "seven");
        hourStrings.put(8, "eight");
        hourStrings.put(9, "nine");
        hourStrings.put(10, "ten");
        hourStrings.put(11, "eleven");
        hourStrings.put(12, "twelve");
        hourStrings.put(0, "all");

        hourInts.put("one",13);
        hourInts.put("two", 14);
        hourInts.put("three", 15);
        hourInts.put("four", 16);
        hourInts.put("five", 17);
        hourInts.put("six", 18);
        hourInts.put("seven", 19);
        hourInts.put("eight", 8);
        hourInts.put("nine", 9);
        hourInts.put("ten", 10);
        hourInts.put("eleven", 11);
        hourInts.put("twelve", 12);
        hourInts.put("all", 0);
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
        l = new ArrayList();
        l.add("three");
        l.add("8");
        hoursMap.put(15, l);
        l = new ArrayList();
        l.add("four");
        l.add("9");
        hoursMap.put(16, l);
        l = new ArrayList();
        l.add("five");
        l.add("10");
        hoursMap.put(17, l);
        l = new ArrayList();
        l.add("six");
        l.add("11");
        hoursMap.put(18, l);


    }

    public void enableStrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    private void updateHoursInDB(ClassroomObject occupiedClassroom, DatabaseReference hoursRef, String hour){
        String freeUntil = occupiedClassroom.getFreeUntil();
        int freeUntilInt = 0;
        int firstHour = 100;
        if (!freeUntil.equals("All Day")){
            freeUntilInt = hourInts.get(freeUntil);
        }
        if (!hour.equals("all")){
            firstHour = hourInts.get(hour);
        }
        int updateUntil = 20;
        if (firstHour<freeUntilInt) {
            updateUntil = freeUntilInt;
        }
        for (int i = firstHour; i < updateUntil; i++) {
            hour = hourStrings.get(i);
            hoursRef.child(hour).setValue(1);
        }
    }

    private boolean finalCheckForClass(DatabaseReference hourRef, String hour,  Integer value){

        final Integer[] val = new Integer[1];
        final boolean[] occupied = {false};
        ValueEventListener hourListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("***********************" + dataSnapshot.getValue(Integer.class));
                val[0] = dataSnapshot.getValue(Integer.class);
                if(val[0] == 1) {
                    occupied[0] = true;
                }
                System.out.println("In data changed");
                Toast.makeText(ViewFreeSpaces.this, "Value is " + val[0], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        hourRef.addListenerForSingleValueEvent(hourListener);



        return occupied[0];//(value[0].equals("0"));
    }

    private void scheduleNotification(Context context, int hour){
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Time's up");
        builder.setContentText("You have left 10 minutes in your current classroom");
        builder.setSmallIcon(R.mipmap.unispace_logo);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.HOUR_OF_DAY, hour);
        calendar.set(calendar.MINUTE, 50);
        calendar.set(calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}



                /*DatabaseReference room = building.child(Integer.toString(occupiedClassroom.getClassroom()));
                DatabaseReference hours = room.child("hours");
                int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3;
                DatabaseReference hour = hours.child(hoursMap.get(time).get(1)).child(hoursMap.get(time).get(0));
                hour.setValue("1");*/

