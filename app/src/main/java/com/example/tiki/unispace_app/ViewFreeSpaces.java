package com.example.tiki.unispace_app;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ViewFreeSpaces extends AppCompatActivity {

    private FirebaseHandler firebaseHandler;
    private RecyclerView mRecyclerView;
    private ClassAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ClassroomObject> classroomObjects;

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
           // classroomObjects = firebaseHandler.GetAllClassrooms();
        } else {
           // classroomObjects = firebaseHandler.GetClassroomsByBuilding(Integer.parseInt(request));
        }
        //TO TEST:
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
        classroomObjects.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));

        mAdapter = new ClassAdapter(classroomObjects);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {

            @Override
            public void onOccupyClick(int position) {
                classroomObjects.get(position).TEST_CHANGE("OCCUPIED!");
                mAdapter.notifyItemChanged(position);
            }
        });
    }





    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
