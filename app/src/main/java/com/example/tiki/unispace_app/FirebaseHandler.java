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
            public ArrayList<ClassroomObject> GetNearestClassrooms (Location location) throws
            ExecutionException, InterruptedException {
        /*StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "lat=" + location.getLatitude() + "&long=" + location.getLongitude());*/
                String locationString = "location=" + Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude());/*"location=32.070470, 34.844486"; //todo parse location to string*/
                ArrayList<ClassroomObject> objects = new ArrayList<>();
                ArrayList<String> checkedBuildings = new ArrayList<>();
                StringBuilder allBuildings = new StringBuilder();
                String checked = "checked=";
                do {
                    MyAsyncTask task = new MyAsyncTask();
                    AsyncTask<String, String, StringBuffer> response = task.execute("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByLocation",
                            "byLocation", locationString, checked + allBuildings);
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
                    for (int i = 0; i < arr.size(); i++) {
                        JsonElement currBuilding = arr.get(i);
                        objects = GetClassroomsByBuilding("building=" + currBuilding.getAsString());
                        if (objects.size() > 0) {
                            return objects;
                        }
                        checkedBuildings.add(currBuilding.getAsString());
                        allBuildings.append(currBuilding.getAsString());
                        allBuildings.append("-");
                    }
                } while (objects.size() == 0 || checkedBuildings.size() != 34);
                //return convertJsonToObjects(jsonArr.getAsJsonObject());
                return objects;
            }




            /**
             * Retrieves the classrooms according to a certain building
             * @return all classrooms in object form
             */
                public ArrayList<ClassroomObject> GetClassroomsByBuilding (String requestedBuilding) throws
                ExecutionException, InterruptedException {
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


                public ArrayList<ClassroomObject> GetAllClassrooms () throws
                ExecutionException, InterruptedException {
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


            private StringBuffer GeneralRequest (String url_Request, String parameter){
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
                    if (parameter != null) {
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

            private ArrayList<ClassroomObject> convertJsonToObjects (JsonObject jsonArray){
                ArrayList<ClassroomObject> allClasses = new ArrayList<>();
                Gson gson = new Gson();
                Iterator<Map.Entry<String, JsonElement>> it = jsonArray.entrySet().iterator();
                //looping through buildings
                while (it.hasNext()) {
                    Map.Entry<String, JsonElement> currentBuilding = it.next();
                    JsonArray rooms = currentBuilding.getValue().getAsJsonArray();
                    //for each building looping through rooms
                    for (int i = 0; i < rooms.size(); i++) {
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


            private class MyAsyncTask extends AsyncTask<String, String, StringBuffer> {

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
                        } else if (strings[1].equals("byLocation")) {
                            con.getOutputStream().write((strings[2] + "&" + strings[3]).getBytes());
                            //con.getOutputStream().write(strings[3].getBytes());
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
