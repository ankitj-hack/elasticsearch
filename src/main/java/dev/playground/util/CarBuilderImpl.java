package dev.playground.util;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import dev.playground.domain.Car;

class CarBuilderImpl implements CarBuilder {

    private Car car = new Car();

    @Override
    public CarBuilder setName(String name) {
        car.setName(name);
        return this;
    }

    @Override
    public CarBuilder setLocation(double x, double y) {
        car.setLocation(new GeoPoint(x, y));
        return this;
    }

    @Override
    public Car build() {
        return car;
    }

}