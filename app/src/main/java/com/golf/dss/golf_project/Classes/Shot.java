package com.golf.dss.golf_project.Classes;

public class Shot {
    private double lat_begin;
    private double long_begin;
    private double lat_end;
    private double long_end;
    private double distance;

    public Shot(double lat_begin, double long_begin, double lat_end, double long_end, double distance) {
        this.lat_begin = lat_begin;
        this.long_begin = long_begin;
        this.lat_end = lat_end;
        this.long_end = long_end;
        this.distance = distance;
    }

    public double getLat_begin() {
        return lat_begin;
    }

    public void setLat_begin(double lat_begin) {
        this.lat_begin = lat_begin;
    }

    public double getLong_begin() {
        return long_begin;
    }

    public void setLong_begin(double long_begin) {
        this.long_begin = long_begin;
    }

    public double getLat_end() {
        return lat_end;
    }

    public void setLat_end(double lat_end) {
        this.lat_end = lat_end;
    }

    public double getLong_end() {
        return long_end;
    }

    public void setLong_end(double long_end) {
        this.long_end = long_end;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
