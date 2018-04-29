package com.example.tiki.unispace_app;

/**
 * Created by Tiki on 4/25/18.
 */

public class Coordinates {

    private double longitude;
    private double latitude;

    public Coordinates(double lo, double la) {
        longitude = lo;
        latitude = la;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double GetDistance(Coordinates c) {
        double myDistance = Math.sqrt(Math.abs(c.getLongitude()-this.longitude)*Math.abs((c.getLongitude()-this.longitude))
                + Math.abs(c.getLatitude()-this.latitude)*Math.abs(c.getLatitude()-this.latitude));
        return myDistance;
    }
}
