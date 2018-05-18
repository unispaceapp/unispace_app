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
        buildings.add("100");
        buildings.add("101");
        buildings.add("102");
        buildings.add("103");
        buildings.add("104");
        buildings.add("105");
        buildings.add("106");
        buildings.add("107");
        buildings.add("108");
        buildings.add("109");
        buildings.add("110");

        buildings.add("201");
        buildings.add("202");
        buildings.add("203");
        buildings.add("204");
        buildings.add("205");
        buildings.add("206");
        buildings.add("207");
        buildings.add("208");
        buildings.add("209");
        buildings.add("211");
        buildings.add("212");
        buildings.add("213");
        buildings.add("214");
        buildings.add("215");
        buildings.add("216");
        buildings.add("217");

        buildings.add("300");
        buildings.add("301");
        buildings.add("302");
        buildings.add("303");
        buildings.add("304");
        buildings.add("305");
        buildings.add("306");
        buildings.add("307");

        buildings.add("401");
        buildings.add("402");
        buildings.add("403");
        buildings.add("404");
        buildings.add("405");
        buildings.add("406");
        buildings.add("407");
        buildings.add("408");
        buildings.add("409");
        buildings.add("410");
        buildings.add("411");

        buildings.add("501");
        buildings.add("502");
        buildings.add("503");
        buildings.add("504");
        buildings.add("505");
        buildings.add("506");
        buildings.add("507");
        buildings.add("508");
        buildings.add("509");

        buildings.add("605");
        buildings.add("604");

        buildings.add("901");
        buildings.add("902");
        buildings.add("905");

        buildings.add("1002");
        buildings.add("1004");

        buildings.add("1102");
        buildings.add("1103");
        buildings.add("1104");
        buildings.add("1105");

    }
}
