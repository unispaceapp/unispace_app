package com.example.tiki.unispace_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;

    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // REQUESTS PERMISSION TO USE USER GPS IF NOT ALREADY ACTIVATED
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location loc = getLocation();
            Intent intent = new Intent(this, ViewFreeSpaces.class);
            intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(loc));
            startActivity(intent);

        }

    }

    @Override
    public void onClick(View view) {
        // REQUESTS PERMISSION TO USE USER GPS IF NOT ALREADY ACTIVATED
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location loc = getLocation();
            Intent intent = new Intent(this, ViewFreeSpaces.class);
            intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(loc));
            startActivity(intent);

        }
    }


    /**
     * Gets location of user
     * @return location of user
     */
    private Location getLocation() {
        // REQUESTS PERMISSION TO USE USER GPS IF NOT ALREADY ACTIVATED
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                return location;

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                return location1;

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                return location2;

            } else {
                //If cannot find user's location, displays error message
                Toast.makeText(this, "Unble to Trace your location", Toast.LENGTH_SHORT).show();

            }
        }
        return null;
    }

    /**
     * Informs the user that they must activate their GPS services
     */
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}