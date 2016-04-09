package dev.playground.util;

import dev.playground.domain.Car;

public interface CarBuilder {

    CarBuilder setName(String name);

    CarBuilder setLocation(double x, double y);

    Car build();

}