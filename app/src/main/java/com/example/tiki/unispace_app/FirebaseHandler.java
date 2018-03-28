package com.example.tiki.unispace_app;
import com.firebase.client.Firebase;
import com.google.gson.Gson;
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

public class FirebaseHandler {


    public FirebaseHandler() {

    }

    public ArrayList<ClassroomObject> GetClassroomsByBuilding(int buildingNum) {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                "building=" + buildingNum);
        System.out.println("*** RESPONSE *** " + response.toString());
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        return convertJsonToObjects(jsonArr);
    }


    public ArrayList<ClassroomObject> GetAllClassrooms() {
        StringBuffer response = GeneralRequest("https://us-central1-unispace-198015.cloudfunctions.net/requestAllClassrooms",
                null);
        System.out.println("*** RESPONSE *** " + response.toString());
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(response.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("*** JSONARRAY: *** " + jsonArr);
        return convertJsonToObjects(jsonArr);
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

    private ArrayList<ClassroomObject> convertJsonToObjects(JSONArray jsonArray) {
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
    }
}
