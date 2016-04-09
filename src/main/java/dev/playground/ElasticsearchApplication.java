package dev.playground;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import dev.playground.domain.Car;
import dev.playground.domain.CarRepository;
import dev.playground.util.Cars;

@SpringBootApplication
@EnableElasticsearchRepositories
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ElasticsearchTemplate elasticsearchTemplate, CarRepository carRepository) {
        return String -> {
            Stream.of(new Car("Audi TT", 10, 10), new Car("Mercedes Class A", 15, 15), new Car("Volvo", 20, 20), new Car("BMW", 25, 25))
                  .forEach(carRepository::save);

            Car car = Cars.builder()
                          .setName("Renault")
                          .setLocation(5, 5)
                          .build();
            carRepository.save(car);

            Iterable<Car> cars = carRepository.findAll();
            System.out.println("--- All cars");
            StreamSupport.stream(cars.spliterator(), false)
                         .forEach(System.out::println);

            System.out.println("--- Find cars");
            // CriteriaQuery geoLocationCriteriaQuery = new CriteriaQuery(new Criteria("location").within(new GeoPoint(5, 5), "10km"));
            // elasticsearchTemplate.queryForList(geoLocationCriteriaQuery, Car.class)
            // .forEach(System.out::println);

            carRepository.findByLocationNear(new GeoPoint(5, 5), "1000km")
                         .forEach(System.out::println);

            carRepository.deleteAll();
        };
    }
}
