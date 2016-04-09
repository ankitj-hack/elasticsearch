package dev.playground.domain;

import java.util.List;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepository extends ElasticsearchRepository<Car, Long> {

    Car findOneByName(String name);

    List<Car> findByLocationNear(GeoPoint point, String distance);

}
