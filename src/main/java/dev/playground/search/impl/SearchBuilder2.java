package dev.playground.search.impl;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Factor;

public class SearchBuilder2 {

    private static final String FACTORS = "factors";

    private static final String FACTORS_FACTOR = "factors.factor";

    private static final String FACTORS_VALUE = "factors.value";

    public SearchQuery buildOrQuery(CustomerProfile customerProfile, String distance) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        customerProfile.getFactors()
                       .stream()
                       .map(e -> createFactorQuery(e, distance))
                       .forEach(queryBuilder::withQuery);

        return queryBuilder.build();
    }

    private NestedQueryBuilder createFactorQuery(Factor factor, String distance) {
        return QueryBuilders.nestedQuery(FACTORS, QueryBuilders.filteredQuery(QueryBuilders.matchQuery(FACTORS_FACTOR, factor.getFactor()),
                                                                              createValueFilter(factor, distance)));
    }

    private GeoDistanceFilterBuilder createValueFilter(Factor factor, String distance) {
        return FilterBuilders.geoDistanceFilter(FACTORS_VALUE)
                             .point(factor.getValue()
                                          .getLat(),
                                    0)
                             .distance(distance)
                             .filterName("factor-value-" + factor.getFactor());
    }

    private GeoDistanceSortBuilder createValueSort(double value) {
        return SortBuilders.geoDistanceSort(FACTORS_VALUE)
                           .point(value, 0)
                           .order(SortOrder.ASC);
    }

}
