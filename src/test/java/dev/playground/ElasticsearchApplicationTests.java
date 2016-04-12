package dev.playground;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ElasticsearchApplication.class)
public abstract class ElasticsearchApplicationTests {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;

}
