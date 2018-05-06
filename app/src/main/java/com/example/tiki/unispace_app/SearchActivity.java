package com.example.tiki.unispace_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity  implements TextWatcher {
    private ArrayList buildings = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bybuilding);
        init();
        EditText searcher = (EditText)findViewById(R.id.building);
        searcher.addTextChangedListener(this);
        Button buildingButton = (Button)findViewById(R.id.search);
        buildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewFreeSpaces.class);
                EditText et = (EditText)findViewById(R.id.building);
                String wantedBuilding = et.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, "BUILDING" + wantedBuilding);
                startActivity(intent);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Toast.makeText(this, "BEFORE", Toast.LENGTH_SHORT).show();
        Button buildingButton = (Button)findViewById(R.id.search);
        buildingButton.setEnabled(false);
            buildingButton.getBackground().setAlpha(100);

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(buildings.contains(charSequence)){
            Button buildingButton = (Button)findViewById(R.id.search);
            buildingButton.setEnabled(true);
            buildingButton.getBackground().setAlpha(255);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(buildings.contains(editable.toString())){
            Button buildingButton = (Button)findViewById(R.id.search);
            buildingButton.setEnabled(true);
            buildingButton.getBackground().setAlpha(255);
        }
    }


    private void init() {
        buildings.add("507");
        buildings.add("505");
        buildings.add("504");
        buildings.add("605");
        buildings.add("604");
        buildings.add("213");
        buildings.add("1004");
        //TODO: Finish


    }
}
