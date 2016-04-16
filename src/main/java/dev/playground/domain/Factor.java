package dev.playground.domain;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public class Factor {

    private int factor;

    private GeoPoint value;

    public Factor() {
    }

    public Factor(int factor, double value) {
        this.factor = factor;
        this.value = new GeoPoint(value, 0);
    }

    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public GeoPoint getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "[factor=" + factor + ", value=" + value.getLat() + "]";
    }

}
