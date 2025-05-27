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

    }

    @Test
    void filteredIndexPartitions() {
        List<IndexPartition> partitions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            partitions.add(new IndexPartition(
                    "partition-" + i,
                    "tenant1",
                    "/path/to/file" + i,
                    2010 + (i % 5), // yearStart from 2010 to 2014
                    2010 + (i % 5)
            ));
        }

        mongoTemplate.insertAll(partitions);


        List<List<String>> actualPartitionIdsBySubQuery = service.findIndexPartitions(new QueryFilter("tenant1", 2011, 2013));
        assertNotNull(actualPartitionIdsBySubQuery);

        List<List<String>> expectedPartitionIdsBySubQuery = List.of(
                List.of("partition-1", "partition-2"),
                List.of("partition-3", "partition-6"),
                List.of("partition-7", "partition-8"));

        assertEquals(3, actualPartitionIdsBySubQuery.size());
        assertEquals(expectedPartitionIdsBySubQuery, actualPartitionIdsBySubQuery);
    }

    @Test
    void filteredIndexPartitionsWithOneIncompleteGroup() {
        List<IndexPartition> partitions = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            partitions.add(new IndexPartition(
                    "partition-" + i,
                    "tenant1",
                    "/path/to/file" + i,
                    2010 + (i % 5), // yearStart from 2010 to 2014
                    2010 + (i % 5)
            ));
        }

        mongoTemplate.insertAll(partitions);


        List<List<String>> actualPartitionIdsBySubQuery = service.findIndexPartitions(new QueryFilter("tenant1", 2011, 2013));
        assertNotNull(actualPartitionIdsBySubQuery);

        List<List<String>> expectedPartitionIdsBySubQuery = List.of(
                List.of("partition-1", "partition-2"),
                List.of("partition-3", "partition-6"),
                List.of("partition-7", "partition-8"),
                List.of("partition-11")
                );

        assertEquals(4, actualPartitionIdsBySubQuery.size());
        assertEquals(expectedPartitionIdsBySubQuery, actualPartitionIdsBySubQuery);
    }
}