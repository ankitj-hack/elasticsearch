package dev.playground.parse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import dev.playground.domain.Factor;
import dev.playground.domain.Product;

public class ProductCsvParser extends CsvParser<Product> {

    @Override
    protected Product mapToProduct(List<String> items) {
        return new Product().setProductId(items.get(0))
                            .setFactors(mapToFactors(items));
    }

    private List<Factor> mapToFactors(List<String> items) {
        return IntStream.range(1, 4)
                        .mapToObj(e -> createFactor(e, items.get(e + 1)))
                        .collect(Collectors.toList());
    }

    private Factor createFactor(int factor, String value) {
        return new Factor(factor, Double.parseDouble(value));
    }

}
