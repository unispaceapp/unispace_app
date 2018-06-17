package com.example.tiki.unispace_app;
import android.location.Location;
import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Handles all logic related to retrieving information
 * from the Firebase database and creating classroom
 * objects to display in the app
 */
public class FirebaseHandler {

    public FirebaseHandler() {

    }


    /**
     * Gets the classroom nearest to the users GPS location
     * @param location users location
     * @return returns the classrooms in object form
     */
    public ArrayList<ClassroomObject> GetNearestClassrooms(Location location) {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "lat=" + location.getLatitude() + "&long=" + location.getLongitude());
        JsonElement jsonArr = null;
        try {
            jsonArr = new JsonParser().parse(response.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }


    /**
     * Retrieves the classrooms according to a certain building
     * @param buildingNum building to find classrooms in
     * @return all classrooms in object form
     */
    public ArrayList<ClassroomObject> GetClassroomsByBuilding(int buildingNum) {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "building=" + buildingNum);
        JsonElement jsonArr = null;
        try {
            jsonArr = new JsonParser().parse(response.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }


    /**
     * Retrieves all free classrooms on entire campus
     * @return classrooms in object form
     */
    public ArrayList<ClassroomObject> GetAllClassrooms() {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                null);
        JsonElement jsonArr = null;
            jsonArr = new JsonParser().parse(response.toString());
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }


    /**
     * Makes an HTTP request to the server
     * @param url_Request which request to make
     * @param parameter parameters to send with the request
     * @return JSON of all the classrooms
     */
    private StringBuffer GeneralRequest(String url_Request, String parameter) {
        URL url = null;
        try {
            url = new URL(url_Request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            if(parameter != null) {
                con.getOutputStream().write(parameter.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
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
        return content;
    }

    /**
     * Converts the JSON of classrooms to an array of clasroom objects
     * @param jsonArray json Array of classrooms retrieved from server
     * @return array of objects
     */
    private ArrayList<ClassroomObject> convertJsonToObjects(JsonObject jsonArray) {
        ArrayList<ClassroomObject> allClasses = new ArrayList<>();
        Gson gson = new Gson();
        Iterator<Map.Entry<String, JsonElement>> it = jsonArray.entrySet().iterator();
        //looping through buildings
        while (it.hasNext()) {
            Map.Entry<String, JsonElement> currentBuilding= it.next();
            JsonArray rooms = currentBuilding.getValue().getAsJsonArray();
            //for each building looping through rooms
            for(int i = 0; i < rooms.size(); i++) {
                try {
                    ClassroomObject c = gson.fromJson(rooms.get(i).toString(), ClassroomObject.class);
                    allClasses.add(c);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

        return allClasses;
    }
}
