package dev.playground.domain;

import java.util.ArrayList;
import java.util.List;

public class CustomerProfile {

    private List<Factor> factors = new ArrayList<>();

    public CustomerProfile() {
    }

    public CustomerProfile add(Factor factor) {
        factors.add(factor);
        return this;
    }

    public Factor get(int factor) {
        return factors.get(factor);
    }

    public List<Factor> getFactors() {
        return new ArrayList<>(factors);
    }

    @Override
    public String toString() {
        return "CustomerProfile [" + factors + "]";
    }

}
