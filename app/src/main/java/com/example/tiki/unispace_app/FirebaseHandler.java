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

public class FirebaseHandler {

    public FirebaseHandler() {

    }


    public ArrayList<ClassroomObject> GetNearestClassrooms(Location location) throws ExecutionException, InterruptedException {
        /*StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "lat=" + location.getLatitude() + "&long=" + location.getLongitude());*/
        String locationString = "location=" + Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude());/*"location=32.070470, 34.844486"; //todo parse location to string*/
        ArrayList<ClassroomObject> objects = new ArrayList<>();
        MyAsyncTask task = new MyAsyncTask();
        AsyncTask<String, String, StringBuffer> response = task.execute("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByLocation",
                "byLocation", locationString);
        System.out.println("*** RESPONSE *** " + response.get().toString());
        JSONArray jsonAr = null;
        JsonElement jsonArr = null;
        try {
            jsonArr = new JsonParser().parse(response.get().toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        JsonArray arr = jsonArr.getAsJsonArray();
        for (int i=0; i<arr.size(); i++){
            JsonElement currBuilding = arr.get(i);
            objects = GetClassroomsByBuilding("building="+currBuilding.getAsString());
            if (objects.size()>0){
                return objects;
            }
        }
        //return convertJsonToObjects(jsonArr.getAsJsonObject());
        return objects;
    }


    public ArrayList<ClassroomObject> GetClassroomsByBuilding(String requestedBuilding) throws ExecutionException, InterruptedException {
        /*StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                requestedBuilding);*/
        MyAsyncTask task = new MyAsyncTask();
        AsyncTask<String, String, StringBuffer> response = task.execute("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "byBuilding", requestedBuilding);
        System.out.println("*** RESPONSE *** " + response.get().toString());
        JSONArray jsonAr = null;
        JsonElement jsonArr = null;
        try {
            jsonArr = new JsonParser().parse(response.get().toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }


    public ArrayList<ClassroomObject> GetAllClassrooms() throws ExecutionException, InterruptedException {
        /*StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                null);*/
        MyAsyncTask task = new MyAsyncTask();
        AsyncTask<String, String, StringBuffer> response = task.execute("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                "all");
        //System.out.println("*** RESPONSE *** " + response.get().toString());
        JSONArray jsonAr = null;
        JsonElement jsonArr = null;
        try {
            jsonArr = new JsonParser().parse(response.get().toString());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }



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

    /*private ArrayList<ClassroomObject> convertJsonToObjects(JSONArray jsonArray) {
        ArrayList<ClassroomObject> allClasses = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                ClassroomObject c = gson.fromJson(jsonArray.get(i).toString(), ClassroomObject.class);
                allClasses.add(c);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return allClasses;
    }*/

    private void InitializeCoordinates() {

//
//        building_coordinates = new HashMap<>();
//
//        building_coordinates.put(100, new Coordinates(32.065480, 34.840948));
//        building_coordinates.put(105, new Coordinates(32.066028, 34.841728));
//        building_coordinates.put(109, new Coordinates(32.066514, 34.841721));
//
//        building_coordinates.put(201, new Coordinates(32.066496, 34.840938));
//        building_coordinates['203'];
//        building_coordinates['204'];
//        building_coordinates['205'];
//        building_coordinates['206'];
//        building_coordinates['207'];
//        building_coordinates['208'];
//        building_coordinates['209'];
//        building_coordinates['210'];
//        building_coordinates['211'];
//        building_coordinates['212'];
//        building_coordinates['213'];
//        building_coordinates['214'];
//        building_coordinates['215'];
//        building_coordinates['216'];
//        building_coordinates['217'];
//
//
//        building_coordinates.put(301, new Coordinates());
//        building_coordinates.put(302, new Coordinates());
//        building_coordinates.put(304, new Coordinates());
//        building_coordinates.put(305, new Coordinates());
//        building_coordinates.put(306, new Coordinates());
//
//
//        building_coordinates.put(401, new Coordinates());
//        building_coordinates.put(402, new Coordinates());
//        building_coordinates.put(403, new Coordinates());
//        building_coordinates.put(404, new Coordinates());
//        building_coordinates.put(405, new Coordinates());
//        building_coordinates.put(407, new Coordinates());
//        building_coordinates.put(408, new Coordinates());
//        building_coordinates.put(409, new Coordinates());
//        building_coordinates.put(410, new Coordinates());
//        building_coordinates.put(411, new Coordinates());





    }

    private class MyAsyncTask extends AsyncTask<String, String, StringBuffer>{

        @Override
        protected StringBuffer doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                if (strings[1].equals("byBuilding")) {
                    con.getOutputStream().write(strings[2].getBytes());
                } else if (strings[1].equals("byLocation")){
                    con.getOutputStream().write(strings[2].getBytes());
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
//            return null;
        }

    }
}
