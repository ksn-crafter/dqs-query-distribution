package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartitionCustomRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import jakarta.annotation.Resource;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class IndexPartitionCustomRepositoryImpl implements IndexPartitionCustomRepository {

    @Value("${spring.data.mongodb.index-partitions.group-by}")
    private int batchSize;

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public List<List<String>> findIndexPartitions(QueryFilter filter) {
        // TODO: 1 partition -> 1 index file -> 1 year of data (MAX)

        MatchOperation match = Aggregation.match(Criteria.where("tenantId").is(filter.tenantId())
                .and("yearStart").gte(filter.yearStart())
                .and("yearEnd").lte(filter.yearEnd()));


        ProjectionOperation project = Aggregation.project("_id");


        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "indexPartition", Document.class);


        List<String> allPartitionIds = results.getMappedResults().stream()
                .map(doc -> doc.getString("_id"))
                .collect(Collectors.toList());


        List<List<String>> partitions = new ArrayList<>();
        for (int i = 0; i < allPartitionIds.size(); i += batchSize) {
            partitions.add(allPartitionIds.subList(i, Math.min(i + batchSize, allPartitionIds.size())));
        }

        return partitions;
    }

}