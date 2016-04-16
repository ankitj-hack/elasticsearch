package dev.playground.search.impl;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.NestedFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Factor;

@Service
public class SearchBuilder {

    private static final String FACTORS = "factors";

    private static final String FACTORS_FACTOR = "factors.factor";

    private static final String FACTORS_VALUE = "factors.value";

    public SearchQuery buildAndQuery(CustomerProfile customerProfile, String distance) {
        List<Factor> factors = customerProfile.getFactors();
        Factor factor1 = factors.get(0);
        Factor factor2 = factors.get(1);
        Factor factor3 = factors.get(2);

        GeoDistanceFilterBuilder valueFilter1 = createValueFilter(factor1, distance);
        NestedFilterBuilder nestedFilter1 = FilterBuilders.nestedFilter(FACTORS, FilterBuilders.andFilter(createFactorFilter(factor1),
                                                                                                          valueFilter1));

        GeoDistanceFilterBuilder valueFilter2 = createValueFilter(factor2, distance);
        NestedFilterBuilder nestedFilter2 = FilterBuilders.nestedFilter(FACTORS, FilterBuilders.andFilter(createFactorFilter(factor2),
                                                                                                          valueFilter2));

        GeoDistanceFilterBuilder valueFilter3 = createValueFilter(factor3, distance);
        NestedFilterBuilder nestedFilter3 = FilterBuilders.nestedFilter(FACTORS, FilterBuilders.andFilter(createFactorFilter(factor3),
                                                                                                          valueFilter3));

        BoolQueryBuilder query = QueryBuilders.boolQuery();

        query.must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), nestedFilter1))
             .must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), nestedFilter2))
             .must(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), nestedFilter3));

        return new NativeSearchQueryBuilder().withQuery(query)
                                             .withSort(createValueSort(factor1, valueFilter1))
                                             .withSort(createValueSort(factor1, valueFilter2))
                                             .withSort(createValueSort(factor1, valueFilter3))
                                             .build();
    }

    private FilteredQueryBuilder createFilteredQuery(Factor factor0, String distance) {
        return QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), createNestedFilters(factor0, distance));
    }

    private NestedFilterBuilder createNestedFilters(Factor factor, String distance) {
        return FilterBuilders.nestedFilter(FACTORS, createFactorAndValueFilters(factor, distance));
    }

    private FilterBuilder createFactorAndValueFilters(Factor factor, String distance) {
        return FilterBuilders.andFilter(createFactorFilter(factor), createValueFilter(factor, distance));
    }

    private FilterBuilder createFactorFilter(Factor factor) {
        return FilterBuilders.termFilter(FACTORS_FACTOR, factor.getFactor());
    }

    private GeoDistanceFilterBuilder createValueFilter(Factor factor, String distance) {
        return FilterBuilders.geoDistanceFilter(FACTORS_VALUE)
                             .point(factor.getValue()
                                          .getLat(),
                                    0)
                             .distance(distance);
    }
    // private FilterBuilder createFactorsFilters(List<Factor> factors, String distance) {
    // return FilterBuilders.andFilter(factors.stream()
    // .map(e -> createFactorFilter(e, distance))
    // .toArray(size -> new FilterBuilder[size]));
    // }

    // private FilterBuilder createFactorFilter(Factor factor, String distance) {
    // return FilterBuilders.andFilter(FilterBuilders.termFilter(FACTORS_FACTOR, factor.getFactor()), createValueFilter(factor, distance));
    // }

    private GeoDistanceSortBuilder createValueSort(Factor factor, FilterBuilder nestedFilter) {
        return SortBuilders.geoDistanceSort(FACTORS_VALUE)
                           .setNestedFilter(nestedFilter)
                           .point(factor.getValue()
                                        .getLat(),
                                  0)
                           .order(SortOrder.ASC);
    }

}
