package dev.playground.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "car", type = "car", shards = 1, replicas = 0, refreshInterval = "-1")
public class Car {

    @Id
    private String id;

    private String name;

    private GeoPoint location;

    public Car() {
    }

    public Car(String name, double x, double y) {
        this.name = name;
        location = new GeoPoint(x, y);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Car [id=" + id + ", name=" + name + ", location=" + location + "]";
    }

}
