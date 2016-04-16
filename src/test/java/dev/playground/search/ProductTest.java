package dev.playground.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import dev.playground.ElasticsearchApplicationTests;
import dev.playground.domain.CustomerProfile;
import dev.playground.domain.Factor;
import dev.playground.domain.Product;
import dev.playground.domain.ProductRepository;
import dev.playground.parse.ProductCsvParser;

public class ProductTest extends ElasticsearchApplicationTests {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SearchService searchService;

    @Before
    public void before() {
        ProductCsvParser parser = new ProductCsvParser();
        try {
            Resource resource = resourceLoader.getResource("classpath:products.csv");
            parser.parse(resource.getFile())
                  .forEach(productRepository::save);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        elasticsearchTemplate.deleteIndex(Product.class);
    }

    @Test
    public void testProductIndexing() {
        findAllProducts();
        findSimilarToCustomerProfile();
    }

    private void findAllProducts() {
        log.info("--- All products\n");
        Iterable<Product> products = productRepository.findAll();
        StreamSupport.stream(products.spliterator(), false)
                     .map(Product::toString)
                     .forEach(System.out::println);
    }

    private void findSimilarToCustomerProfile() {
        // CustomerProfile customerProfile = new CustomerProfile().add(new Factor(1, 0.272727))
        // .add(new Factor(2, 0.454545))
        // .add(new Factor(3, 0.090909));

        CustomerProfile customerProfile = new CustomerProfile().add(new Factor(1, 0.333333))
                                                               .add(new Factor(2, 0.444444))
                                                               .add(new Factor(3, 0.0));

        System.out.println("\n\n--- Similar products to " + customerProfile + "\n");
        List<Product> products = searchService.findSimilar(customerProfile, "0.1km");
        // List<Product> products = searchService.findSimilar(customerProfile);
        products.forEach(System.out::println);
    }

}
