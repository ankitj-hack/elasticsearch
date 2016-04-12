package dev.playground.domain;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public class Dimension {

    private String dimension;

    private GeoPoint location;

    public Dimension() {
    }

    public Dimension(String dimension, GeoPoint location) {
        this.dimension = dimension;
        this.location = location;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Dimension [dimension=" + dimension + ", location=(" + location.getLat() + "," + location.getLon() + ")]";
    }

}
