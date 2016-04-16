package dev.playground.search;

import java.util.List;

import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Product;

public interface SearchService {

    List<Product> findSimilar(CustomerProfile customerProfile, String distance);

    List<Product> findSimilar(CustomerProfile customerProfile);

}
