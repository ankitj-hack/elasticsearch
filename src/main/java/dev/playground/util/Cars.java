package dev.playground.util;

public class Cars {

    private Cars() {
    }

    public static CarBuilder builder() {
        return new CarBuilderImpl();
    }

}
