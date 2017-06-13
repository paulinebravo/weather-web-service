package fr.wcs.weatherwebservice;

import com.google.api.client.util.Key;

public class Coord {
    @Key
    private Double lat;
    @Key
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}