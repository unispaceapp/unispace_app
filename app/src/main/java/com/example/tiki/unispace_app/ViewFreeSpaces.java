package com.example.tiki.unispace_app;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class ViewFreeSpaces extends AppCompatActivity {

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
        setContentView(R.layout.activity_view_free_spaces);
        firebaseHandler = new FirebaseHandler();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        res = new ArrayList<>();
        Intent intent = getIntent();
        String request = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(request.equals("ALL")) {
           // res = firebaseHandler.GetAllClassrooms();
        } else {
           // res = firebaseHandler.GetClassroomsByBuilding(Integer.parseInt(request));
        }
        //TO TEST:
        res.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 104, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 104, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 104, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 63, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(604, 104, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(507, 101, "ten", R.drawable.ic_home_black_24dp));
        res.add(new ClassroomObject(402, 12, "ten", R.drawable.ic_home_black_24dp));

        mAdapter = new ClassAdapter(res);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {

            @Override
            public void onOccupyClick(int position) {
                res.get(position).TEST_CHANGE("OCCUPIED!");
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
