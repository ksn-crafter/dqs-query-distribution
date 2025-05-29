package com.dqs.eventdrivensearch.queryDistribution.consumer;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartition;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.service.IndexPartitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilteredIndexPartitionsIntegrationTest {


    @Autowired
    private IndexPartitionService service;

    @BeforeEach
    void setUp() {
        service.deleteAll();

    }

    @Test
    void filteredIndexPartitions() {
        List<IndexPartition> partitions = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int year = 2010 + (i % 5);

            partitions.add(new IndexPartition(
                    "partition-" + i,
                    "tenant1",
                    "/path/to/file-" + i + "(" + year + ")",
                    year,
                    year
            ));
        }

        service.insertAll(partitions);

        List<List<String>> actualFilePathsBySubQuery = service.findIndexPartitions(new QueryFilter("tenant1", 2011, 2013));
        assertNotNull(actualFilePathsBySubQuery);

        List<List<String>> expectedFilePathsBySubQuery = List.of(
                List.of("/path/to/file-1(2011)", "/path/to/file-2(2012)"),
                List.of("/path/to/file-3(2013)", "/path/to/file-6(2011)"),
                List.of("/path/to/file-7(2012)", "/path/to/file-8(2013)"));

        assertEquals(expectedFilePathsBySubQuery, actualFilePathsBySubQuery);
    }

    @Test
    void filteredIndexPartitionsWithOneIncompleteGroup() {
        List<IndexPartition> partitions = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            int year = 2010 + (i % 5);

            partitions.add(new IndexPartition(
                    "partition-" + i,
                    "tenant1",
                    "/path/to/file-" + i + "(" + year + ")",
                    year,
                    year
            ));
        }

        service.insertAll(partitions);

        List<List<String>> actualFilePathsBySubQuery = service.findIndexPartitions(new QueryFilter("tenant1", 2011, 2013));
        assertNotNull(actualFilePathsBySubQuery);

        List<List<String>> expectedFilePathsBySubQuery = List.of(
                List.of("/path/to/file-1(2011)", "/path/to/file-2(2012)"),
                List.of("/path/to/file-3(2013)", "/path/to/file-6(2011)"),
                List.of("/path/to/file-7(2012)", "/path/to/file-8(2013)"),
                List.of("/path/to/file-11(2011)")
                );

        assertEquals(expectedFilePathsBySubQuery, actualFilePathsBySubQuery);
    }
}