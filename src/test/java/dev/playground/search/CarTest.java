package dev.playground.search;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.DistanceUnit.Distance;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import dev.playground.ElasticsearchApplicationTests;
import dev.playground.domain.Car;
import dev.playground.domain.CarRepository;

public class CarTest extends ElasticsearchApplicationTests {

    @Autowired
    private CarRepository carRepository;

    @Before
    public void before() {
        Stream.of(new Car("Audi TT", 10, 10), new Car("Mercedes Class A", 15, 15), new Car("Volvo", 20, 20), new Car("BMW", 25, 25),
                  new Car("Renault", 5, 5))
              .forEach(carRepository::save);
    }

    @After
    public void after() {
        elasticsearchTemplate.deleteIndex(Car.class);
    }

    @Test
    public void testCarIndexing() {
        findAllCars();
        findCarsByLocationWithTemplate();
        findCarsByLocationWithRepository();
    }

    private void findAllCars() {
        log.info("--- All cars");
        Iterable<Car> cars = carRepository.findAll();
        StreamSupport.stream(cars.spliterator(), false)
                     .map(Car::toString)
                     .forEach(log::info);
    }

    private void findCarsByLocationWithTemplate() {
        log.info("--- Find cars by location with template");
        CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(new Criteria("location").within(new GeoPoint(5, 5), "10km"));
        elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, Car.class)
                             .forEach(System.out::println);
    }

    private void findCarsByLocationWithRepository() {
        log.info("--- Find cars by location with repository");
        carRepository.findByLocationNear(new GeoPoint(5, 5), new Distance(10, DistanceUnit.KILOMETERS).toString())
                     .forEach(System.out::println);

        log.info("--- Find cars by location with repository");
        carRepository.findByLocationNear(new GeoPoint(5, 5), "1000km")
                     .forEach(System.out::println);
    }
}
