package com.example.tiki.unispace_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ViewFreeSpaces extends AppCompatActivity {

    private StorageReference s;
    private FirebaseFunctions mFunctions;
// ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_free_spaces);

        //Intent intent = getIntent();
        //String message = intent.getStringExtra(Intent.EXTRA_TEXT);
        Button b = (Button)findViewById(R.id.allSpacesButton);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                enableStrictMode();
                //TODO CHANGED HERE openActivity(view);
                setContentView(R.layout.activity_list_all_classrooms);
                asyncTask ay = new asyncTask();
                ay.execute();
                //final TextView textView = findViewById(R.id.textView);
                //textView.setText(addMessage("asd").getResult());

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private JSONArray getAllClassrooms() {


        URL url = null;
        try {
            url = new URL("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        StringBuffer content = new StringBuffer();
        try {
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        con.disconnect();
        System.out.println("*** CONTENT *** : " + content);
        JSONArray jsonArr = null;
        try {
             jsonArr = new JSONArray(content.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("json: " + jsonArr);
        return jsonArr;

    }


    private class asyncTask extends AsyncTask<URL, Integer, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(URL... urls) {
            JSONArray s = getAllClassrooms();
            return s.toString();
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            TextView list = (TextView)findViewById(R.id.listOfClassrooms);
            list.setText(result);

        }
    }


    public void openActivity(View view) {
        Intent intent = new Intent(this, ListAllClassrooms.class);
        //intent.putExtra(Intent.EXTRA_TEXT, tryit());
        startActivity(intent);
    }


    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
