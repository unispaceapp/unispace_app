package com.example.tiki.unispace_app;
import com.firebase.client.Firebase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class FirebaseHandler {


    public FirebaseHandler() {

    }

    public ArrayList<ClassroomObject> GetClassroomsByBuilding(int buildingNum) {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/classroomsByBuilding",
                "building=" + buildingNum);
        System.out.println("*** RESPONSE *** " + response.toString());
        JSONArray jsonAr = null;
        JsonElement jsonArr = null;
        try {
            //jsonArr = new JSONArray(response.toString());
            jsonArr = new JsonParser().parse(response.toString());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        return convertJsonToObjects(jsonArr.getAsJsonObject());
    }


    public ArrayList<ClassroomObject> GetAllClassrooms() {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                null);
        System.out.println("*** RESPONSE *** " + response.toString());
        JSONArray jsonAr = null;
        JsonElement jsonArr = null;
        //try {
            //jsonArr = new JSONArray(response.toString());
            jsonArr = new JsonParser().parse(response.toString());

        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/
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
}
