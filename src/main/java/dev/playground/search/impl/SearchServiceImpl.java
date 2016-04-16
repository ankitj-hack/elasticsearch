package dev.playground.search.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Product;
import dev.playground.domain.ProductRepository;
import dev.playground.search.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

    private static int DISTANCE = 100;

    private static String DISTANCE_SYMBOL = "m";

    @Autowired
    private SearchBuilder searchBuilder;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findSimilar(CustomerProfile customerProfile) {
        int attempts = 0;
        int distance = DISTANCE;
        List<Product> products = new ArrayList<>();
        while (products.size() < 5 && attempts < 10000) {
            products = findSimilar(customerProfile, distance + DISTANCE_SYMBOL);
            distance += DISTANCE;
            attempts++;
        }
        return products.stream()
                       .limit(5)
                       .collect(Collectors.toList());
    }

    @Override
    public List<Product> findSimilar(CustomerProfile customerProfile, String distance) {
        SearchQuery query = searchBuilder.buildAndQuery(customerProfile, distance);
        FacetedPage<Product> search = productRepository.search(query);
        return StreamSupport.stream(search.spliterator(), false)
                            .collect(Collectors.toList());
    }

}
