package dev.playground.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "airplane", type = "airplane")
public class Airplane {

    @Id
    private String id;

    private String name;

    @Field(type = FieldType.Nested)
    private List<Dimension> dimensions;

    public Airplane() {
    }

    public Airplane(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public String toString() {
        return "Airplane [id=" + id + ", name=" + name + ", dimensions=" + dimensions + "]";
    }

}
