package dev.playground.search.impl;

import java.util.List;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Factor;

public class SearchBuilder3 {

    private static final String FACTORS = "factors";

    private static final String FACTORS_FACTOR = "factors.factor";

    private static final String FACTORS_VALUE = "factors.value";

    public SearchQuery buildOrQuery(CustomerProfile customerProfile, String distance) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        FilterBuilder factorFilters = createFactorsFilters(customerProfile.getFactors(), distance);
        FilteredQueryBuilder filteredQuery = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), factorFilters);
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery(FACTORS, filteredQuery);
        queryBuilder.withQuery(nestedQuery);

        return queryBuilder.build();
    }

    private FilterBuilder createFactorsFilters(List<Factor> factors, String distance) {
        Factor factor0 = factors.get(0);
        Factor factor1 = factors.get(1);
        return FilterBuilders.andFilter(createFactorFilter(factor0, distance), createFactorFilter(factor1, distance));
    }

    private FilterBuilder createFactorFilter(Factor factor, String distance) {
        TermFilterBuilder termFilter = FilterBuilders.termFilter(FACTORS_FACTOR, factor.getFactor());
        GeoDistanceFilterBuilder distanceFilter = createValueFilter(factor, distance);
        return FilterBuilders.andFilter(termFilter, distanceFilter);
    }

    // private FilterBuilder createFactorsFilters(List<Factor> factors, String distance) {
    // return FilterBuilders.andFilter(factors.stream()
    // .map(e -> createFactorFilter(e, distance))
    // .toArray(size -> new FilterBuilder[size]));
    // }

    // private FilterBuilder createFactorFilter(Factor factor, String distance) {
    // return FilterBuilders.andFilter(FilterBuilders.termFilter(FACTORS_FACTOR, factor.getFactor()), createValueFilter(factor, distance));
    // }

    private GeoDistanceFilterBuilder createValueFilter(Factor factor, String distance) {
        return FilterBuilders.geoDistanceFilter(FACTORS_VALUE)
                             .point(factor.getValue()
                                          .getLat(),
                                    0)
                             .distance(distance);
    }

    // private GeoDistanceSortBuilder createValueSort(double value) {
    // return SortBuilders.geoDistanceSort(FACTORS_VALUE)
    // .point(value, 0)
    // .order(SortOrder.ASC);
    // }

}
