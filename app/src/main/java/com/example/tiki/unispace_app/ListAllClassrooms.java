package com.example.tiki.unispace_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ListAllClassrooms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_classrooms);

        asyncTask ay = new asyncTask();
        ay.execute();
    }


    private JSONObject getAllClassrooms() {


        URL url = null;
        try {
            url = new URL("https://us-central1-unispace-198015.cloudfunctions.net/dailyDBUpdate");
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
        JSONObject jo = null;
        System.out.println("*** CONTENT *** : " + content);
        try {
            JSONObject jsonObj = new JSONObject(content.toString());
            System.out.println("*** BUILDING *** : " + jsonObj.get("building"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            jo = new JSONObject(content.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("json: " + jo.toString());
        return jo;

    }
    private class asyncTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            TextView list = (TextView)findViewById(R.id.textView2);
            JSONObject s = getAllClassrooms();
            return s.toString();
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            TextView list = (TextView)findViewById(R.id.textView2);
            list.setText(result);

        }
    }
    }
