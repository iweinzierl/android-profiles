package de.iweinzierl.easyprofiles.persistence;

import com.google.common.base.MoreObjects;
import com.orm.dsl.Table;

@Table(name = "location")
public class Location {

    private Long id;

    private String name;

    private double lat;
    private double lon;

    private int radius;

    public Location() {}

    public Location(String name, double lat, double lon, int radius) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("lat", lat)
                .add("lon", lon)
                .add("radius", radius)
                .toString();
    }
}
