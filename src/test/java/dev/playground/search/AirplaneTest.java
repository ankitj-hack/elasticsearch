package dev.playground.search;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import dev.playground.ElasticsearchApplicationTests;
import dev.playground.domain.Airplane;
import dev.playground.domain.AirplaneRepository;
import dev.playground.domain.Dimension;

public class AirplaneTest extends ElasticsearchApplicationTests {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Before
    public void before() {

        Stream.of(airplane("Greyfire", 5d), airplane("Shadowcomet", 10d), airplane("Roguepyre", 15d), airplane("Darkthunder", 20d),
                  airplane("Evil Crow", 25d), airplane("Dark Dart", 27d), airplane("Swift Owl", 28d), airplane("Freemaster", 29d))
              .forEach(airplaneRepository::save);
    }

    private Airplane airplane(String name, Double position) {
        Dimension x = new Dimension("x", new GeoPoint(position, 0));
        Dimension y = new Dimension("y", new GeoPoint(position * 2, 0));
        Dimension z = new Dimension("z", new GeoPoint(position * 3, 0));
        Airplane airplane = new Airplane(name);
        airplane.setDimensions(Arrays.asList(x, y, z));
        return airplane;
    }

    @After
    public void after() {
        elasticsearchTemplate.deleteIndex(Airplane.class);
    }

    @Test
    public void testAirplaneNestedIndexing() {
        findAllAirplanes();
        findOneByName();
        findAirplanesByMultipleLocationsWithTemplate();
        findAirplanesByMultipleLocationsSortedByLocationWithTemplate();
    }

    private void findAllAirplanes() {
        log.info("--- All airplanes");
        Iterable<Airplane> airplanes = airplaneRepository.findAll();
        StreamSupport.stream(airplanes.spliterator(), false)
                     .map(Airplane::toString)
                     .forEach(log::info);
    }

    private void findOneByName() {
        log.info("--- Find one airplane by name");
        Airplane airplane = airplaneRepository.findOneByName("Shadowcomet");
        log.info(airplane.toString());
    }

    private void findAirplanesByMultipleLocationsWithTemplate() {
        log.info("--- Find airplanes by multiple locations with template");

        String distance = "1000km";
        double latitude = 27d;
        double longitude = 0;

        GeoDistanceFilterBuilder filterLocationX = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryX = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "x"),
                                                                                                filterLocationX));

        GeoDistanceFilterBuilder filterLocationY = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude * 2, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryY = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "y"),
                                                                                                filterLocationY));

        GeoDistanceFilterBuilder filterLocationZ = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude * 3, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryZ = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "z"),
                                                                                                filterLocationZ));

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(nestedQueryX)
                                                                .withQuery(nestedQueryY)
                                                                .withQuery(nestedQueryZ)
                                                                .build();

        airplaneRepository.search(searchQuery)
                          .forEach(System.out::println);
    }

    private void findAirplanesByMultipleLocationsSortedByLocationWithTemplate() {
        log.info("--- Find airplanes by multiple locations sorted by location with template");

        String distance = "100km";
        double latitude = 27d;
        double longitude = 0;

        GeoDistanceFilterBuilder filterLocationX = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryX = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "x"),
                                                                                                filterLocationX));

        GeoDistanceFilterBuilder filterLocationY = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude * 2, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryY = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "y"),
                                                                                                filterLocationY));

        GeoDistanceFilterBuilder filterLocationZ = FilterBuilders.geoDistanceFilter("dimensions.location")
                                                                 .point(latitude * 3, longitude)
                                                                 .distance(distance);

        NestedQueryBuilder nestedQueryZ = QueryBuilders.nestedQuery("dimensions",
                                                                    QueryBuilders.filteredQuery(QueryBuilders.matchQuery("dimensions.dimension",
                                                                                                                         "z"),
                                                                                                filterLocationZ));

        GeoDistanceSortBuilder sortLocation = SortBuilders.geoDistanceSort("dimensions.location")
                                                          .point(latitude, 0)
                                                          .order(SortOrder.ASC);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(nestedQueryX)
                                                                .withQuery(nestedQueryY)
                                                                .withQuery(nestedQueryZ)
                                                                .withSort(sortLocation)
                                                                .build();

        airplaneRepository.search(searchQuery)
                          .forEach(System.out::println);
    }

}
