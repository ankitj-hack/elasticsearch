package dev.playground.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "product", type = "product")
public class Product {

    @Id
    private String id;

    private String productId;

    @Field(type = FieldType.Nested)
    private List<Factor> factors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public Product setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public List<Factor> getFactors() {
        return factors;
    }

    public Product setFactors(List<Factor> factors) {
        this.factors = factors;
        return this;
    }

    @Override
    public String toString() {
        return "Product [" + productId + ", " + factors + "]";
    }

}
