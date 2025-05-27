package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartition;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.service.IndexPartitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilteredIndexPartitionsIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IndexPartitionService service;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(IndexPartition.class);

        List<IndexPartition> partitions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            partitions.add(new IndexPartition(
                    "partition-" + i,
                    "tenant1",
                    "/path/to/file" + i,
                    2010 + (i % 5), // yearStart from 2010 to 2014
                    2010 + (i % 5)
            ));
        }

        mongoTemplate.insertAll(partitions);
        var mongoPartitions = mongoTemplate.findAll(IndexPartition.class);
        System.out.println(mongoPartitions);
    }

    @Test
    void filteredIndexPartitions() {
        List<List<String>> partitionIdsBySubquery = service.findIndexPartitions(new QueryFilter("tenant1", 2011, 2015));

        assertNotNull(partitionIdsBySubquery);
        assertEquals(4, partitionIdsBySubquery.get(0).size());
    }
}
