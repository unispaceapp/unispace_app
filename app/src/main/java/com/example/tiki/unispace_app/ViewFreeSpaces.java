package com.example.tiki.unispace_app;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DateTimeKeyListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewFreeSpaces extends AppCompatActivity {

    private FirebaseHandler firebaseHandler;
    private RecyclerView mRecyclerView;
    private ClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClassroomObject> classroomObjects;
    DatabaseReference dbRootRef = FirebaseDatabase.getInstance().getReference();
    private Map<Integer, List<String>> hoursMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_free_spaces);
        firebaseHandler = new FirebaseHandler();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        classroomObjects = new ArrayList<>();
        Intent intent = getIntent();
        String request = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(request.equals("ALL")) {
            classroomObjects = firebaseHandler.GetAllClassrooms();
        } else {
            classroomObjects = firebaseHandler.GetClassroomsByBuilding(Integer.parseInt(request));
        }

        for (int i=0; i<classroomObjects.size(); i++){
            classroomObjects.get(i).setIcon(R.drawable.ic_home_black_24dp);
        }
        /*//TO TEST:
        classroomObjects.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 104, "five", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(507, 101, "eight", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 63, "four", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 104, "twelve", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(402, 12, "nine", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 104, "six", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(402, 12, "twelve", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(604, 104, "eleven", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(507, 101, "seven", R.drawable.ic_home_black_24dp));
        classroomObjects.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));*/

        mAdapter = new ClassAdapter(classroomObjects);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        ArrayList l = new ArrayList();
        l.add("seven");
        l.add("0");
        hoursMap.put(7,l);
        l = new ArrayList();
        l.add("eight");
        l.add("1");
        hoursMap.put(8,l);
        l = new ArrayList();
        l.add("nine");
        l.add("2");
        hoursMap.put(9,l);
        l = new ArrayList();
        l.add("ten");
        l.add("3");
        hoursMap.put(10,l);
        l = new ArrayList();
        l.add("eleven");
        l.add("4");
        hoursMap.put(11,l);
        l = new ArrayList();
        l.add("twelve");
        l.add("5");
        hoursMap.put(12,l);
        l = new ArrayList();
        l.add("one");
        l.add("6");
        hoursMap.put(13,l);
        l = new ArrayList();
        l.add("two");
        l.add("7");
        hoursMap.put(14,l);

        mAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {

            @Override
            public void onOccupyClick(int position) {
                final ClassroomObject occupiedClassroom = classroomObjects.get(position);
                occupiedClassroom.TEST_CHANGE("OCCUPIED!");
                mAdapter.notifyItemChanged(position);
                DatabaseReference building = dbRootRef.child(Integer.toString(occupiedClassroom.getBuilding()));
                building.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                Iterator<DataSnapshot> childrenIt = children.iterator();
                                while (childrenIt.hasNext()){
                                    DataSnapshot current = childrenIt.next();
                                    if (current.child("classroom").getValue()==occupiedClassroom.getClassroom()){
                                        DataSnapshot hours = current.child("hours");
                                        int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3;
                                        DataSnapshot hour = hours.child(hoursMap.get(time).get(1)).child(hoursMap.get(time).get(0));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                /*DatabaseReference room = building.child(Integer.toString(occupiedClassroom.getClassroom()));
                DatabaseReference hours = room.child("hours");
                int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3;
                DatabaseReference hour = hours.child(hoursMap.get(time).get(1)).child(hoursMap.get(time).get(0));
                hour.setValue("1");*/
            }
        });
    }





    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
